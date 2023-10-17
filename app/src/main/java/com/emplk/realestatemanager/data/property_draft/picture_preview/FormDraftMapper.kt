package com.emplk.realestatemanager.data.property_draft.picture_preview

import com.emplk.realestatemanager.data.property_draft.BaseFormDraftDto
import com.emplk.realestatemanager.data.property_draft.amenity.AmenityDraftDto
import com.emplk.realestatemanager.data.property_draft.amenity.AmenityDraftMapper
import com.emplk.realestatemanager.domain.property_draft.FormDraftEntity
import javax.inject.Inject

class FormDraftMapper @Inject constructor(
    private val picturePreviewMapper: PicturePreviewMapper,
    private val amenityDraftMapper: AmenityDraftMapper,
) {

    fun mapToPropertyDraftDto(propertyForm: FormDraftEntity): BaseFormDraftDto =
        BaseFormDraftDto(
            id = propertyForm.id,
            type = propertyForm.type,
            price = propertyForm.price,
            surface = propertyForm.surface,
            address = propertyForm.address,
            isAddressValid = propertyForm.isAddressValid,
            rooms = propertyForm.rooms,
            bedrooms = propertyForm.bedrooms,
            bathrooms = propertyForm.bathrooms,
            description = propertyForm.description,
            agentName = propertyForm.agentName,
        )

    fun mapToPropertyDraftEntity(
        baseFormDraftDto: BaseFormDraftDto,
        picturePreviewDtos: List<PicturePreviewDto>,
        amenityDraftDtos: List<AmenityDraftDto>,
    ): FormDraftEntity =
        FormDraftEntity(
            id = baseFormDraftDto.id,
            type = baseFormDraftDto.type,
            price = baseFormDraftDto.price,
            surface = baseFormDraftDto.surface,
            address = baseFormDraftDto.address,
            isAddressValid = baseFormDraftDto.isAddressValid,
            rooms = baseFormDraftDto.rooms,
            bedrooms = baseFormDraftDto.bedrooms,
            bathrooms = baseFormDraftDto.bathrooms,
            description = baseFormDraftDto.description,
            agentName = baseFormDraftDto.agentName,
            pictures = picturePreviewMapper.mapToPicturePreviewEntities(picturePreviewDtos),
            amenities = amenityDraftMapper.mapToAmenityFormEntities(amenityDraftDtos),
        )
}