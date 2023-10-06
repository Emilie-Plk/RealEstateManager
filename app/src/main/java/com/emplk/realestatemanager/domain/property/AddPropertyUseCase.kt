package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.domain.geocoding.GeocodingRepository
import com.emplk.realestatemanager.domain.geocoding.GeocodingWrapper
import com.emplk.realestatemanager.domain.map_picture.GenerateMapBaseUrlWithParamsUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.location.LocationEntity
import com.emplk.realestatemanager.domain.property.pictures.PictureEntity
import com.emplk.realestatemanager.domain.property_form.AddPropertyFormEntity
import com.emplk.realestatemanager.domain.property_form.ClearPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.GetPicturePreviewsUseCase
import java.time.Clock
import java.time.LocalDateTime
import javax.inject.Inject

class AddPropertyUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val geocodingRepository: GeocodingRepository,
    private val generateMapBaseUrlWithParamsUseCase: GenerateMapBaseUrlWithParamsUseCase,
    private val clearPropertyFormUseCase: ClearPropertyFormUseCase,
    private val getPicturePreviewsUseCase: GetPicturePreviewsUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val clock: Clock,
) {
    suspend fun invoke(form: AddPropertyFormEntity): Boolean {
        if (form.propertyType != null &&
            form.address != null &&
            form.price != null &&
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

            val success = propertyRepository.addPropertyWithDetails(
                PropertyEntity(
                    type = form.propertyType,
                    price = form.price.toBigDecimal(),
                    surface = form.surface.toInt(),
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
                            uri = it.uri,
                            description = it.description ?: "",
                            isFeatured = it.id == form.featuredPictureId,
                        )
                    },
                    entryDate = LocalDateTime.now(clock),
                    isAvailableForSale = false,
                    isSold = false,
                    saleDate = null,
                ),
            )

           return if (success) {
                clearPropertyFormUseCase.invoke()
                setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                true
            } else {
                return false
            }
        } else {
            return false
        }
    }
}
