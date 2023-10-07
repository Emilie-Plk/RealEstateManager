package com.emplk.realestatemanager.data.property_draft.amenity

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import javax.inject.Inject

class AmenityDraftMapper @Inject constructor() {
    fun mapToAmenityFormEntity(amenityDraftDto: AmenityDraftDto) =
        AmenityEntity(
            id = amenityDraftDto.id,
            type = AmenityType.valueOf(amenityDraftDto.name),
        )

    fun mapToAmenityDto(amenityEntity: AmenityEntity, propertyFormId: Long) =
        AmenityDraftDto(
            id = amenityEntity.id,
            propertyFormId = propertyFormId,
            name = amenityEntity.type.name,
        )

    fun mapToAmenityFormEntities(amenityDraftDtos: List<AmenityDraftDto>): List<AmenityEntity> =
        amenityDraftDtos.map { mapToAmenityFormEntity(it) }
}
