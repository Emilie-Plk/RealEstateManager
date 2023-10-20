package com.emplk.realestatemanager.data.property_draft.picture_preview

import com.emplk.realestatemanager.data.property_draft.FormDraftDto
import com.emplk.realestatemanager.data.property_draft.amenity.AmenityDraftDto
import com.emplk.realestatemanager.data.property_draft.amenity.AmenityDraftMapper
import com.emplk.realestatemanager.domain.property_draft.FormDraftEntity
import javax.inject.Inject

class FormDraftMapper @Inject constructor(
    private val picturePreviewMapper: PicturePreviewMapper,
    private val amenityDraftMapper: AmenityDraftMapper,
) {

    fun mapToPropertyDraftDto(propertyForm: FormDraftEntity): FormDraftDto =
        FormDraftDto(
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
            isSold = propertyForm.isSold,
            saleDate = propertyForm.saleDate,
        )

    fun mapToPropertyDraftEntity(
        formDraftDto: FormDraftDto,
        picturePreviewDtos: List<PicturePreviewDto>,
        amenityDraftDtos: List<AmenityDraftDto>,
    ): FormDraftEntity =
        FormDraftEntity(
            id = formDraftDto.id,
            type = formDraftDto.type,
            price = formDraftDto.price,
            surface = formDraftDto.surface,
            address = formDraftDto.address,
            isAddressValid = formDraftDto.isAddressValid,
            rooms = formDraftDto.rooms,
            bedrooms = formDraftDto.bedrooms,
            bathrooms = formDraftDto.bathrooms,
            description = formDraftDto.description,
            agentName = formDraftDto.agentName,
            pictures = picturePreviewMapper.mapToPicturePreviewEntities(picturePreviewDtos),
            amenities = amenityDraftMapper.mapToAmenityFormEntities(amenityDraftDtos),
            isSold = formDraftDto.isSold,
            saleDate = formDraftDto.saleDate,
        )
}