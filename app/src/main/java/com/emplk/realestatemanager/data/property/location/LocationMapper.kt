package com.emplk.realestatemanager.data.property.location

import com.emplk.realestatemanager.domain.location.LocationEntity
import javax.inject.Inject

class LocationMapper @Inject constructor() {

    fun mapToDtoEntity(locationEntity: LocationEntity, propertyId: Long) =
        LocationDto(
            id = locationEntity.id,
            propertyId = propertyId,
            address = locationEntity.address,
            miniatureMapPath = locationEntity.miniatureMapPath,
            latitude = locationEntity.latitude,
            longitude = locationEntity.longitude,
        )

    fun mapToDomainEntity(locationDto: LocationDto) =
        LocationEntity(
            id = locationDto.id,
            propertyId = locationDto.propertyId,
            address = locationDto.address,
            miniatureMapPath = locationDto.miniatureMapPath,
            latitude = locationDto.latitude,
            longitude = locationDto.longitude,
        )
}