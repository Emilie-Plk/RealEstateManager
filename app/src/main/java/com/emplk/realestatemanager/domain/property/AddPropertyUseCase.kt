package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.data.property.PropertyDtoEntity
import javax.inject.Inject

class AddPropertyUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
) {
    suspend fun invoke(property: PropertyDtoEntity): Long {
        return propertyRepository.add(property)
    }
}