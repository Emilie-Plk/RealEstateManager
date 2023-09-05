package com.emplk.realestatemanager.data.location

import com.emplk.realestatemanager.domain.location.LocationEntity
import javax.inject.Inject

class LocationDtoEntityMapper @Inject constructor() {

    fun mapToDtoEntity(locationEntity: LocationEntity) =
        LocationDtoEntity(
            id = locationEntity.id,
            propertyId = locationEntity.propertyId,
            address = locationEntity.address,
            city = locationEntity.city,
            postalCode = locationEntity.postalCode,
            latitude = locationEntity.latitude,
            longitude = locationEntity.longitude,
        )

    fun mapToDomainEntity(locationDtoEntity: LocationDtoEntity) =
        LocationEntity(
            id = locationDtoEntity.id,
            propertyId = locationDtoEntity.propertyId,
            address = locationDtoEntity.address,
            city = locationDtoEntity.city,
            postalCode = locationDtoEntity.postalCode,
            latitude = locationDtoEntity.latitude,
            longitude = locationDtoEntity.longitude,
        )
}