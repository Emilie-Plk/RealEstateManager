package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import javax.inject.Inject

class UpdatePropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
) {
    suspend fun invoke(form: FormDraftStateEntity) {
        formDraftRepository.update(
            form.id?.let {
                FormDraftEntity(
                    id = it,
                    type = form.propertyType,
                    price = form.price,
                    surface = form.surface,
                    description = form.description,
                    rooms = form.nbRooms,
                    bathrooms = form.nbBathrooms,
                    address = form.address,
                    isAddressValid = form.isAddressValid,
                    bedrooms = form.nbBedrooms,
                    agentName = form.agent,
                    amenities = form.amenities.map { amenity ->
                        AmenityEntity(
                            id = amenity.id,
                            type = amenity.type,
                        )
                    },
                )
            } ?: throw IllegalStateException("Form id is null")
        )
    }
}