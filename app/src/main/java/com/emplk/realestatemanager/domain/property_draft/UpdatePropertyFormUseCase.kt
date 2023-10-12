package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import javax.inject.Inject

class UpdatePropertyFormUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository,
    private val getCurrentPropertyDraftIdUseCase: GetCurrentPropertyDraftIdUseCase,
) {
    suspend fun invoke(form: PropertyFormStateEntity) {
        getCurrentPropertyDraftIdUseCase.invoke()?.let { currentPropertyFormId ->
            propertyFormRepository.update(
                PropertyDraftEntity(
                    type = form.propertyType,
                    price = form.price,
                    surface = form.surface,
                    description = form.description,
                    rooms = form.nbRooms,
                    bathrooms = form.nbBathrooms,
                    address = form.address,
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