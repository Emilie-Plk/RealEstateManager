package com.emplk.realestatemanager.data.location

import com.emplk.realestatemanager.domain.location.LocationEntity
import javax.inject.Inject

class LocationDtoEntityMapper @Inject constructor() {

    fun mapToDtoEntity(location: LocationEntity): LocationDtoEntity =
        LocationDtoEntity(
            id = location.id,
            propertyId = location.propertyId,
            address = location.address,
            city = location.city,
            postalCode = location.postalCode,
            latitude = location.latitude,
            longitude = location.longitude,
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