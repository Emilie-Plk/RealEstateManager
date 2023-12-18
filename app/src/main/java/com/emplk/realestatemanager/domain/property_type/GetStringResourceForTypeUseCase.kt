package com.emplk.realestatemanager.domain.property_type

import javax.inject.Inject

class GetStringResourceForTypeUseCase @Inject constructor(
    private val propertyTypeRepository: PropertyTypeRepository,
) {
    fun invoke(type: String): Int {
        return propertyTypeRepository.getPropertyTypes()
            .first { it.databaseName == type }
            .stringRes
    }
}