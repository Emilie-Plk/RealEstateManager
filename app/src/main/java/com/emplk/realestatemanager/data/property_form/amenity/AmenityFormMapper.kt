package com.emplk.realestatemanager.data.property_form.amenity

import com.emplk.realestatemanager.domain.property_form.amenity.AmenityFormEntity
import javax.inject.Inject

class AmenityFormMapper @Inject constructor() {
    fun mapToAmenityFormEntity(amenityFormDto: AmenityFormDto, propertyFormId: Long) =
        AmenityFormEntity(
            id = amenityFormDto.id,
            propertyFormId = propertyFormId,
            type = amenityFormDto.name,
        )

    fun mapToAmenityDto(amenityFormEntity: AmenityFormEntity) =
        AmenityFormDto(
            id = amenityFormEntity.id,
            propertyFormId = amenityFormEntity.propertyFormId,
            name = amenityFormEntity.type,
        )

}
