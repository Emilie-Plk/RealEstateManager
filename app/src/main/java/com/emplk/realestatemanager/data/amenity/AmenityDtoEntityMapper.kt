package com.emplk.realestatemanager.data.amenity

import com.emplk.realestatemanager.domain.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.amenity.AmenityType
import javax.inject.Inject

class AmenityDtoEntityMapper @Inject constructor() {

    fun mapToDtoEntity(amenity: AmenityEntity) =
        AmenityDtoEntity(
            id = amenity.id,
            name = amenity.type.name,
            propertyId = amenity.propertyId,
        )

    fun mapToDomainEntities(amenityDtoEntities: List<AmenityDtoEntity>) =
        amenityDtoEntities.map { mapToDomainEntity(it) }

    private fun mapToDomainEntity(amenityDtoEntity: AmenityDtoEntity) =
        AmenityEntity(
            id = amenityDtoEntity.id,
            type = AmenityType.valueOf(amenityDtoEntity.name),
            propertyId = amenityDtoEntity.propertyId,
        )
}