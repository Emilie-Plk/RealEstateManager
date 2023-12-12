package com.emplk.realestatemanager.data.property.location

import com.emplk.realestatemanager.domain.property.location.LocationEntity
import com.emplk.realestatemanager.domain.property.location.PropertyLatLongAndSoldStatusEntity
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class LocationMapper @Inject constructor() {

    fun mapToDto(locationEntity: LocationEntity, propertyId: Long) =
        LocationDto(
            propertyId = propertyId,
            address = locationEntity.address,
            miniatureMapUrl = locationEntity.miniatureMapUrl,
            latitude = locationEntity.latLng?.latitude,
            longitude = locationEntity.latLng?.longitude,
        )

    fun mapToDomainEntity(locationDto: LocationDto) =
        LocationEntity(
            address = locationDto.address,
            miniatureMapUrl = locationDto.miniatureMapUrl,
            latLng = locationDto.latitude?.let { latitude ->
                locationDto.longitude?.let { longitude ->
                    LatLng(latitude, longitude)
                }
            }
        )

    fun mapToPropertyLatLongEntity(propertyWithLatLongAndSaleDateDto: PropertyWithLatLongAndSaleDateDto): PropertyLatLongAndSoldStatusEntity? =
        propertyWithLatLongAndSaleDateDto.latitude?.let { latitude ->
            propertyWithLatLongAndSaleDateDto.longitude?.let { longitude ->
                PropertyLatLongAndSoldStatusEntity(
                    propertyId = propertyWithLatLongAndSaleDateDto.propertyId,
                    latLng = LatLng(latitude, longitude),
                    isSold = propertyWithLatLongAndSaleDateDto.saleDate == null
                )
            }
        }
}