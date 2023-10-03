package com.emplk.realestatemanager.data.property.location

import com.emplk.realestatemanager.domain.property.location.LocationEntity
import com.emplk.realestatemanager.domain.property.location.PropertyLatLongEntity
import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class LocationMapper @Inject constructor() {

    fun mapToDtoEntity(locationEntity: LocationEntity, propertyId: Long) =
        LocationDto(
            propertyId = propertyId,
            address = locationEntity.address,
            miniatureMapPath = locationEntity.miniatureMapPath,
            latitude = locationEntity.latLng?.latitude,
            longitude = locationEntity.latLng?.longitude,
        )

    fun mapToDomainEntity(locationDto: LocationDto) =
        LocationEntity(
            address = locationDto.address,
            miniatureMapPath = locationDto.miniatureMapPath,
            latLng = locationDto.latitude?.let { latitude ->
                locationDto.longitude?.let { longitude ->
                    LatLng(latitude, longitude)
                }
            }
        )

    fun mapToPropertyLatLongEntity(propertyLatLongDto: PropertyLatLongDto): PropertyLatLongEntity? =
        propertyLatLongDto.latitude?.let { latitude ->
            propertyLatLongDto.longitude?.let { longitude ->
                PropertyLatLongEntity(
                    propertyId = propertyLatLongDto.propertyId,
                    latLng = LatLng(latitude, longitude)
                )
            }
        }
}