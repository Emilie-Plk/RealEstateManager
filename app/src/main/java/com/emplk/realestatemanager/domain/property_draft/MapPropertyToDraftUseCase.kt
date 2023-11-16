package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.property.PropertyRepository
import com.emplk.realestatemanager.domain.property_draft.picture_preview.PicturePreviewEntity
import javax.inject.Inject

class MapPropertyToDraftUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
) {
    suspend fun invoke(propertyId: Long): FormDraftEntity {
        val propertyEntity = propertyRepository.getPropertyById(propertyId)
        return FormDraftEntity(
            id = propertyId,
            type = propertyEntity.type,
            title = null,   // TODO: CHANGE that
            price = propertyEntity.price,
            surface = propertyEntity.surface,
            address = propertyEntity.location.address,
            isAddressValid = true,
            rooms = propertyEntity.rooms,
            bedrooms = propertyEntity.bedrooms,
            bathrooms = propertyEntity.bathrooms,
            description = propertyEntity.description,
            agentName = propertyEntity.agentName,
            pictures = propertyEntity.pictures.map { picture ->
                PicturePreviewEntity(
                    id = picture.id,
                    uri = picture.uri,
                    description = picture.description,
                    isFeatured = picture.isFeatured,
                )
            },
            amenities = propertyEntity.amenities.map { it },
            isSold = propertyEntity.isSold,
            entryDate = propertyEntity.entryDate,
            entryDateEpoch = propertyEntity.entryDateEpoch,
            saleDate = propertyEntity.saleDate,
            lastEditionDate = propertyEntity.lastEditionDate,
        )
    }
}

