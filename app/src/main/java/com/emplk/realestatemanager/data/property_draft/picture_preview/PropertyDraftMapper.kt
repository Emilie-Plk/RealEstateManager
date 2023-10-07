package com.emplk.realestatemanager.data.property_draft.picture_preview

import com.emplk.realestatemanager.data.property_draft.PropertyDraftDto
import com.emplk.realestatemanager.data.property_draft.amenity.AmenityDraftDto
import com.emplk.realestatemanager.data.property_draft.amenity.AmenityDraftMapper
import com.emplk.realestatemanager.domain.property_draft.PropertyDraftEntity
import javax.inject.Inject

class PropertyDraftMapper @Inject constructor(
    private val picturePreviewMapper: PicturePreviewMapper,
    private val amenityDraftMapper: AmenityDraftMapper,
) {

    fun mapToPropertyDraftDto(propertyForm: PropertyDraftEntity): PropertyDraftDto =
        PropertyDraftDto(
            type = propertyForm.type,
            price = propertyForm.price,
            surface = propertyForm.surface,
            address = propertyForm.address,
            rooms = propertyForm.rooms,
            bedrooms = propertyForm.bedrooms,
            bathrooms = propertyForm.bathrooms,
            description = propertyForm.description,
            agentName = propertyForm.agentName,
        )

    fun mapToPropertyDraftEntity(
        propertyDraftDto: PropertyDraftDto,
        picturePreviewDtos: List<PicturePreviewDto>,
        amenityDraftDtos: List<AmenityDraftDto>,
    ): PropertyDraftEntity =
        PropertyDraftEntity(
            type = propertyDraftDto.type,
            price = propertyDraftDto.price,
            surface = propertyDraftDto.surface,
            address = propertyDraftDto.address,
            rooms = propertyDraftDto.rooms,
            bedrooms = propertyDraftDto.bedrooms,
            bathrooms = propertyDraftDto.bathrooms,
            description = propertyDraftDto.description,
            agentName = propertyDraftDto.agentName,
            pictures = picturePreviewMapper.mapToPicturePreviewEntities(picturePreviewDtos),
            amenities = amenityDraftMapper.mapToAmenityFormEntities(amenityDraftDtos),
        )
}