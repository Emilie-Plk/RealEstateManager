package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.currency_rate.ConvertEuroToDollarUseCase
import com.emplk.realestatemanager.domain.currency_rate.CurrencyRateWrapper
import com.emplk.realestatemanager.domain.currency_rate.GetCurrencyRateUseCase
import com.emplk.realestatemanager.domain.geocoding.GeocodingRepository
import com.emplk.realestatemanager.domain.geocoding.GeocodingWrapper
import com.emplk.realestatemanager.domain.locale_formatting.GetLocaleUseCase
import com.emplk.realestatemanager.domain.map_picture.GenerateMapBaseUrlWithParamsUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.location.LocationEntity
import com.emplk.realestatemanager.domain.property.pictures.PictureEntity
import com.emplk.realestatemanager.domain.property_draft.ClearPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.FormDraftStateEntity
import com.emplk.realestatemanager.domain.property_draft.UpdatePropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.address.ResetSelectedAddressStateUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.GetPicturePreviewsUseCase
import com.emplk.realestatemanager.ui.add.AddPropertyEvent
import com.emplk.realestatemanager.ui.utils.NativeText
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDateTime
import java.util.Locale
import javax.inject.Inject

class AddPropertyUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val geocodingRepository: GeocodingRepository,
    private val generateMapBaseUrlWithParamsUseCase: GenerateMapBaseUrlWithParamsUseCase,
    private val getLocaleUseCase: GetLocaleUseCase,
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase,
    private val getPicturePreviewsUseCase: GetPicturePreviewsUseCase,
    private val convertEuroToDollarUseCase: ConvertEuroToDollarUseCase,
    private val updatePropertyFormUseCase: UpdatePropertyFormUseCase,
    private val clearPropertyFormUseCase: ClearPropertyFormUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val clock: Clock,
) {
    suspend fun invoke(form: FormDraftStateEntity): AddPropertyEvent {
        require(
            form.id != null &&
                    form.propertyType != null &&
                    form.address != null &&
                    form.price > BigDecimal.ZERO &&
                    form.surface != null &&
                    form.description != null &&
                    form.nbRooms > 0 &&
                    form.nbBathrooms > 0 &&
                    form.nbBedrooms > 0 &&
                    form.agent != null &&
                    form.amenities.isNotEmpty() &&
                    form.pictureIds.isNotEmpty()
        ) {
            "Impossible state: form is not valid => form : $form"
        }
        val addPropertyWrapper: AddPropertyWrapper = coroutineScope {
            val geocodingWrapperDeferred = async {
                geocodingRepository.getLatLong(form.address)
            }
            val currencyWrapperDeferred = async {
                getCurrencyRateUseCase.invoke()
            }

            val success = propertyRepository.addPropertyWithDetails(
                PropertyEntity(
                    id = form.id,
                    type = form.propertyType,
                    price = when (getLocaleUseCase.invoke()) {
                        Locale.US -> form.price
                        Locale.FRANCE -> when (val currencyRateEntity = currencyWrapperDeferred.await()) {
                            is CurrencyRateWrapper.Success -> convertEuroToDollarUseCase.invoke(
                                form.price,
                                currencyRateEntity.currencyRateEntity.usdToEuroRate
                            )

                            is CurrencyRateWrapper.Error -> convertEuroToDollarUseCase.invoke(
                                form.price,
                                currencyRateEntity.fallbackUsToEuroRate
                            )
                        }

                        else -> return@coroutineScope AddPropertyWrapper.LocaleError(NativeText.Resource(R.string.add_property_generic_error_message))
                    },
                    surface = form.surface.toDouble(),
                    description = form.description,
                    rooms = form.nbRooms,
                    bathrooms = form.nbBathrooms,
                    location = getLocationEntity(form.address, geocodingWrapperDeferred.await())
                        ?: return@coroutineScope AddPropertyWrapper.NoLatLong(NativeText.Resource(R.string.add_property_generic_error_message)),
                    bedrooms = form.nbBedrooms,
                    agentName = form.agent,
                    amenities = form.amenities,
                    pictures =
                        getPicturePreviewsUseCase.invoke(form.id).map {
                            PictureEntity(
                                id = it.id,
                                uri = it.uri,
                                description = it.description,
                                isFeatured = it.id == form.featuredPictureId,
                            )
                        },
                    entryDate = LocalDateTime.now(clock),
                    isSold = false,
                    saleDate = null,
                ),
            )
            if (success) {
                AddPropertyWrapper.Success(NativeText.Resource(R.string.add_property_successfully_created_message))
            } else {
                AddPropertyWrapper.Error(NativeText.Resource(R.string.add_property_generic_error_message))
            }
        }
        return when (addPropertyWrapper) {
            is AddPropertyWrapper.Success -> {
                clearPropertyFormUseCase.invoke(form.id)
                setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                AddPropertyEvent.Toast(addPropertyWrapper.text)
            }

            is AddPropertyWrapper.Error -> {
                updatePropertyFormUseCase.invoke(form)
                setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                AddPropertyEvent.Toast(addPropertyWrapper.error)
            }

            is AddPropertyWrapper.NoLatLong -> {
                AddPropertyEvent.Toast(addPropertyWrapper.error)
            }

            is AddPropertyWrapper.LocaleError -> AddPropertyEvent.Toast(addPropertyWrapper.error)
        }
    }

    private fun getLocationEntity(
        formAddress: String,
        geocodingWrapper: GeocodingWrapper
    ): LocationEntity? {  // Wrapper for that to throw error
        return LocationEntity(
            address = formAddress,
            latLng = when (geocodingWrapper) {
                is GeocodingWrapper.Success -> geocodingWrapper.latLng
                is GeocodingWrapper.Error,
                is GeocodingWrapper.NoResult -> null
            },
            miniatureMapUrl = when (geocodingWrapper) {
                is GeocodingWrapper.Success -> generateMapBaseUrlWithParamsUseCase.invoke(geocodingWrapper.latLng)
                is GeocodingWrapper.Error,
                is GeocodingWrapper.NoResult -> return null
            },
        )
    }
}
