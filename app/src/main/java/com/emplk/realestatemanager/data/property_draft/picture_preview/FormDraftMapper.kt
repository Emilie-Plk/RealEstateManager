package com.emplk.realestatemanager.data.property_draft.picture_preview

import com.emplk.realestatemanager.data.property_draft.FormDraftDto
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property_draft.FormDraftEntity
import javax.inject.Inject

class FormDraftMapper @Inject constructor(
    private val picturePreviewMapper: PicturePreviewMapper,
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
            amenitySchool = propertyForm.amenities.contains(AmenityType.SCHOOL),
            amenityPark = propertyForm.amenities.contains(AmenityType.PARK),
            amenityMall = propertyForm.amenities.contains(AmenityType.SHOPPING_MALL),
            amenityRestaurant = propertyForm.amenities.contains(AmenityType.RESTAURANT),
            amenityConcierge = propertyForm.amenities.contains(AmenityType.CONCIERGE),
            amenityGym = propertyForm.amenities.contains(AmenityType.GYM),
            amenityTransportation = propertyForm.amenities.contains(AmenityType.PUBLIC_TRANSPORTATION),
            amenityHospital = propertyForm.amenities.contains(AmenityType.HOSPITAL),
            amenityLibrary = propertyForm.amenities.contains(AmenityType.LIBRARY),
            agentName = propertyForm.agentName,
            isSold = propertyForm.isSold,
            entryDate = propertyForm.entryDate,
            saleDate = propertyForm.saleDate,
            lastEditionDate = propertyForm.lastEditionDate,
        )

    fun mapToPropertyDraftEntity(
        formDraftDto: FormDraftDto,
        picturePreviewDtos: List<PicturePreviewDto>,
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
            amenities = mapAmenities(formDraftDto),
            isSold = formDraftDto.isSold,
            entryDate = formDraftDto.entryDate,
            saleDate = formDraftDto.saleDate,
            lastEditionDate = formDraftDto.lastEditionDate,
        )

    private fun mapAmenities(formDraftDto: FormDraftDto): List<AmenityType> = buildList {
        if (formDraftDto.amenitySchool) add(AmenityType.SCHOOL)
        if (formDraftDto.amenityPark) add(AmenityType.PARK)
        if (formDraftDto.amenityMall) add(AmenityType.SHOPPING_MALL)
        if (formDraftDto.amenityRestaurant) add(AmenityType.RESTAURANT)
        if (formDraftDto.amenityConcierge) add(AmenityType.CONCIERGE)
        if (formDraftDto.amenityGym) add(AmenityType.GYM)
        if (formDraftDto.amenityTransportation) add(AmenityType.PUBLIC_TRANSPORTATION)
        if (formDraftDto.amenityHospital) add(AmenityType.HOSPITAL)
        if (formDraftDto.amenityLibrary) add(AmenityType.LIBRARY)
    }
}