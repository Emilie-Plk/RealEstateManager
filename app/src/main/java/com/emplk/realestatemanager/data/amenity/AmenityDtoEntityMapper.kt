package com.emplk.realestatemanager.data.amenity

import com.emplk.realestatemanager.domain.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.amenity.AmenityType

class AmenityDtoEntityMapper {

    fun mapToDtoEntity(amenities: List<AmenityEntity>, propertyId: Long): List<AmenityDtoEntity> = amenities
        .map {
            AmenityDtoEntity(
                name = it.type.name,
                propertyId = propertyId,
            )
        }


    fun mapToDomainEntity(amenityDtoEntities: List<AmenityDtoEntity>): List<AmenityEntity> = amenityDtoEntities
        .map {
            AmenityEntity(
                id = it.id,
                type = AmenityType.valueOf(it.name),
            )
        }

}