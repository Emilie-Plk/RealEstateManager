package com.emplk.realestatemanager.domain.property_type

import com.emplk.realestatemanager.data.property_type.PropertyType
import javax.inject.Inject

class GetPropertyTypeUseCase @Inject constructor(
    private val propertyTypeRepository: PropertyTypeRepository
) {
    fun invoke(): List<PropertyType> = propertyTypeRepository.getPropertyTypes()
}