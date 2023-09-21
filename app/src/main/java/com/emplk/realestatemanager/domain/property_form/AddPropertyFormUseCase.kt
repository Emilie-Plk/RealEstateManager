package com.emplk.realestatemanager.domain.property_form

import com.emplk.realestatemanager.domain.property.PropertyRepository
import javax.inject.Inject

class AddPropertyFormUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository
) {
}