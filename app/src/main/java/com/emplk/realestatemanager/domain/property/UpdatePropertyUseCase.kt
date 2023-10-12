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
import com.emplk.realestatemanager.domain.property_draft.PropertyFormStateEntity
import com.emplk.realestatemanager.domain.property_draft.picture_preview.GetPicturePreviewsUseCase
import com.emplk.realestatemanager.ui.edit.EditPropertyEvent
import com.emplk.realestatemanager.ui.utils.NativeText
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDateTime
import java.util.Locale
import javax.inject.Inject

class UpdatePropertyUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val geocodingRepository: GeocodingRepository,
    private val getLocaleUseCase: GetLocaleUseCase,
    private val generateMapBaseUrlWithParamsUseCase: GenerateMapBaseUrlWithParamsUseCase,
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase,
    private val convertEuroToDollarUseCase: ConvertEuroToDollarUseCase,
    private val clearPropertyFormUseCase: ClearPropertyFormUseCase,
    private val getPicturePreviewsUseCase: GetPicturePreviewsUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val clock: Clock,
) {
    suspend fun invoke(form: PropertyFormStateEntity) : EditPropertyEvent {
        if (form.propertyType != null &&
            form.address != null &&
            (form.price > BigDecimal.ZERO) &&
            form.surface != null &&
            form.description != null &&
            form.nbRooms > 0 &&
            form.nbBathrooms > 0 &&
            form.nbBedrooms > 0 &&
            form.agent != null &&
            form.amenities.isNotEmpty() &&
            form.pictureIds.isNotEmpty()
        ) {
            val geocodingWrapper = geocodingRepository.getLatLong(form.address)
            when (geocodingWrapper) {
                is GeocodingWrapper.Success -> geocodingWrapper.latLng
                is GeocodingWrapper.Error -> Unit
                is GeocodingWrapper.NoResult -> Unit
            }
            val currencyWrapper = getCurrencyRateUseCase.invoke()
            when (currencyWrapper) {
                is CurrencyRateWrapper.Success -> currencyWrapper.currencyRateEntity.usdToEuroRate
                is CurrencyRateWrapper.Error -> currencyWrapper.fallbackUsToEuroRate
            }

            val success = propertyRepository.update(
                PropertyEntity(
                    type = form.propertyType,
                    price = when (getLocaleUseCase.invoke()) {
                        Locale.US -> form.price
                        Locale.FRANCE -> when (currencyWrapper) {
                            is CurrencyRateWrapper.Success -> convertEuroToDollarUseCase.invoke(
                                form.price,
                                currencyWrapper.currencyRateEntity.usdToEuroRate
                            )

                            is CurrencyRateWrapper.Error -> convertEuroToDollarUseCase.invoke(
                                form.price,
                                currencyWrapper.fallbackUsToEuroRate
                            )
                        }

                        else -> throw Exception("Error while converting price")
                    },
                    surface = form.surface.toDouble(),
                    description = form.description,
                    rooms = form.nbRooms,
                    bathrooms = form.nbBathrooms,
                    location = LocationEntity(
                        address = form.address,
                        latLng = when (geocodingWrapper) {
                            is GeocodingWrapper.Success -> geocodingWrapper.latLng
                            is GeocodingWrapper.Error -> null
                            is GeocodingWrapper.NoResult -> null
                        },
                        miniatureMapUrl = when (geocodingWrapper) {
                            is GeocodingWrapper.Success ->
                                generateMapBaseUrlWithParamsUseCase.invoke(geocodingWrapper.latLng)

                            is GeocodingWrapper.Error -> ""
                            is GeocodingWrapper.NoResult -> ""
                        },
                    ),
                    bedrooms = form.nbBedrooms,
                    agentName = form.agent,
                    amenities = form.amenities,
                    pictures = getPicturePreviewsUseCase.invoke().map {
                        PictureEntity(
                            id = it.id,
                            uri = it.uri,
                            description = it.description ?: "",
                            isFeatured = it.id == form.featuredPictureId,
                        )
                    },
                    entryDate = LocalDateTime.now(clock),
                    isSold = false,
                    saleDate = null,
                ),
            )
            return if (success) {
                clearPropertyFormUseCase.invoke()
                setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                EditPropertyEvent.Toast(NativeText.Resource(R.string.add_property_successfully_updated_message))
            } else {
                EditPropertyEvent.Toast(NativeText.Resource(R.string.add_property_error_message))
            }
        } else {
            return EditPropertyEvent.Toast(NativeText.Resource(R.string.add_property_error_message))
        }
    }
}