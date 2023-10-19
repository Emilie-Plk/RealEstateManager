package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.currency_rate.ConvertEuroToDollarUseCase
import com.emplk.realestatemanager.domain.currency_rate.GetCurrencyRateUseCase
import com.emplk.realestatemanager.domain.geocoding.GeocodingRepository
import com.emplk.realestatemanager.domain.geocoding.GeocodingWrapper
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertToUsdDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetLocaleUseCase
import com.emplk.realestatemanager.domain.map_picture.GenerateMapBaseUrlWithParamsUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.location.LocationEntity
import com.emplk.realestatemanager.domain.property.pictures.PictureEntity
import com.emplk.realestatemanager.domain.property_draft.ClearPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.FormDraftParams
import com.emplk.realestatemanager.domain.property_draft.UpdatePropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.GetPicturePreviewsUseCase
import com.emplk.realestatemanager.ui.add.AddPropertyEvent
import com.emplk.realestatemanager.ui.utils.NativeText
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDateTime
import javax.inject.Inject

class AddPropertyUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val geocodingRepository: GeocodingRepository,
    private val generateMapBaseUrlWithParamsUseCase: GenerateMapBaseUrlWithParamsUseCase,
    private val getLocaleUseCase: GetLocaleUseCase,
    private val convertToUsdDependingOnLocaleUseCase: ConvertToUsdDependingOnLocaleUseCase,
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase,
    private val getPicturePreviewsUseCase: GetPicturePreviewsUseCase,
    private val convertEuroToDollarUseCase: ConvertEuroToDollarUseCase,
    private val convertSurfaceToSquareFeetDependingOnLocaleUseCase: ConvertSurfaceToSquareFeetDependingOnLocaleUseCase,
    private val updatePropertyFormUseCase: UpdatePropertyFormUseCase,
    private val clearPropertyFormUseCase: ClearPropertyFormUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val clock: Clock,
) {
    suspend fun invoke(form: FormDraftParams): AddPropertyEvent {
        require(
            form.propertyType != null &&
                    form.address != null &&
                    form.price > BigDecimal.ZERO &&
                    form.surface > BigDecimal.ZERO &&
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
        val addPropertyWrapper: AddOrEditPropertyWrapper = coroutineScope {
            val geocodingWrapperDeferred = async {
                geocodingRepository.getLatLong(form.address)
            }

            val success = propertyRepository.addPropertyWithDetails(
                PropertyEntity(
                    id = form.id,
                    type = form.propertyType,
                    price = convertToUsdDependingOnLocaleUseCase.invoke(form.price),
                    surface = convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(form.surface),
                    description = form.description,
                    rooms = form.nbRooms,
                    bathrooms = form.nbBathrooms,
                    location = getLocationEntity(form.address, geocodingWrapperDeferred.await())
                        ?: return@coroutineScope AddOrEditPropertyWrapper.NoLatLong(NativeText.Resource(R.string.form_generic_error_message)),
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
                AddOrEditPropertyWrapper.Success(NativeText.Resource(R.string.form_successfully_created_message))
            } else {
                AddOrEditPropertyWrapper.Error(NativeText.Resource(R.string.form_generic_error_message))
            }
        }
        return when (addPropertyWrapper) {
            is AddOrEditPropertyWrapper.Success -> {
                clearPropertyFormUseCase.invoke(form.id)
                setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                AddPropertyEvent.Toast(addPropertyWrapper.text)
            }

            is AddOrEditPropertyWrapper.Error -> {
                updatePropertyFormUseCase.invoke(form)
                setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                AddPropertyEvent.Toast(addPropertyWrapper.error)
            }

            is AddOrEditPropertyWrapper.NoLatLong -> {
                AddPropertyEvent.Toast(addPropertyWrapper.error)
            }

            is AddOrEditPropertyWrapper.LocaleError -> AddPropertyEvent.Toast(addPropertyWrapper.error)
        }
    }

    private fun getLocationEntity(
        formAddress: String,
        geocodingWrapper: GeocodingWrapper
    ): LocationEntity? {
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
