package com.emplk.realestatemanager.data.property.amenity

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import javax.inject.Inject

class AmenityMapper @Inject constructor() {

    fun mapToDtoEntity(amenity: AmenityEntity, propertyId: Long) =
        AmenityDto(
            id = amenity.id,
            name = amenity.type.name,
            propertyId = propertyId,
        )

    fun mapToDtoEntities(amenities: List<AmenityEntity>, propertyId: Long) =
        amenities.map { mapToDtoEntity(it, propertyId) }

    fun mapToDomainEntities(amenityDtoEntities: List<AmenityDto>) =
        amenityDtoEntities.map { mapToDomainEntity(it) }

    private fun mapToDomainEntity(amenityDto: AmenityDto) =
        AmenityEntity(
            id = amenityDto.id,
            type = AmenityType.valueOf(amenityDto.name),
        )
}