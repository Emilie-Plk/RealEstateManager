package com.emplk.realestatemanager.domain.property_form

import com.emplk.realestatemanager.domain.property_form.location.LocationFormEntity
import javax.inject.Inject

class AddTemporaryPropertyFormUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository
) {
    suspend fun invoke(): Long = propertyFormRepository.addPropertyFormWithDetails(
        PropertyFormEntity()
    )
}
