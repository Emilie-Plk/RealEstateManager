package com.emplk.realestatemanager.domain.property_form

import javax.inject.Inject

class AddTemporaryPropertyFormUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository
) {
    suspend fun invoke(): Long = propertyFormRepository.addPropertyFormWithDetails(
        PropertyFormEntity(
            type = "",
            price = "",
            surface = "",
            rooms = 0,
            bedrooms = 0,
            bathrooms = 0,
            description = "",
            address = "",
            agentName = "",
        )
    )
}
