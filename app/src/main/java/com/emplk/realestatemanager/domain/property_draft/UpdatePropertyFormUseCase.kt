package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import javax.inject.Inject

class UpdatePropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val getCurrentDraftIdUseCase: GetCurrentDraftIdUseCase,
) {
    suspend fun invoke(form: FormDraftStateEntity) {
        getCurrentDraftIdUseCase.invoke()?.let { currentPropertyFormId ->
            formDraftRepository.update(
                FormDraftEntity(
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
                ), currentPropertyFormId
            )
        }
    }
}