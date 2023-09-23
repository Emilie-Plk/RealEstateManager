package com.emplk.realestatemanager.data.property_form.amenity

import com.emplk.realestatemanager.domain.property_form.amenity.AmenityFormEntity
import javax.inject.Inject

class AmenityFormMapper @Inject constructor() {
    fun mapToAmenityFormEntity(amenityFormDto: AmenityFormDto) =
        AmenityFormEntity(
            type = amenityFormDto.name,
        )

    fun mapToAmenityDto(amenityFormEntity: AmenityFormEntity, propertyFormId: Long) =
        AmenityFormDto(
            propertyFormId = propertyFormId,
            name = amenityFormEntity.type,
        )

    fun mapToAmenityFormEntities(amenityFormDtos: List<AmenityFormDto>): List<AmenityFormEntity> =
        amenityFormDtos.map { mapToAmenityFormEntity(it) }
}
