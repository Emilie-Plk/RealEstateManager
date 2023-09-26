package com.emplk.realestatemanager.domain.property_form

import com.emplk.realestatemanager.domain.property_form.location.LocationFormEntity
import java.math.BigDecimal
import javax.inject.Inject

class AddTemporaryPropertyFormUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository
) {
    suspend fun invoke(): Long = propertyFormRepository.addPropertyFormWithDetails(
        PropertyFormEntity(
            type = "",
            price = BigDecimal.ZERO,
            surface = 0,
            rooms = 0,
            bedrooms = 0,
            bathrooms = 0,
            description = "",
            location = LocationFormEntity(
                address = "",
                latitude = null,
                longitude = null,
            ),
            agentName = "",
        )
    )
}
