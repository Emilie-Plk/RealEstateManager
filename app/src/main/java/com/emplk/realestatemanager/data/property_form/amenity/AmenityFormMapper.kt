package com.emplk.realestatemanager.data.property_form.amenity

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import javax.inject.Inject

class AmenityFormMapper @Inject constructor() {
    fun mapToAmenityFormEntity(amenityFormDto: AmenityFormDto) =
        AmenityEntity(
            id = amenityFormDto.id,
            type = AmenityType.valueOf(amenityFormDto.name),
        )

    fun mapToAmenityDto(amenityEntity: AmenityEntity, propertyFormId: Long) =
        AmenityFormDto(
            id = amenityEntity.id,
            propertyFormId = propertyFormId,
            name = amenityEntity.type.name,
        )

    fun mapToAmenityFormEntities(amenityFormDtos: List<AmenityFormDto>): List<AmenityEntity> =
        amenityFormDtos.map { mapToAmenityFormEntity(it) }
}
