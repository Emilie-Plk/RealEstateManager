package com.emplk.realestatemanager.domain.property_type

import javax.inject.Inject

class GetPropertyTypeFlowUseCase @Inject constructor(
    private val propertyTypeRepository: PropertyTypeRepository
) {
    fun invoke(): Map<Long, String> = propertyTypeRepository.getPropertyTypes()
}