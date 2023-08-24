package com.emplk.realestatemanager.domain.add_property

import com.emplk.realestatemanager.domain.PropertyRepository
import com.emplk.realestatemanager.domain.entities.PropertyEntity
import javax.inject.Inject

class AddPropertyUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
) {
    suspend fun invoke(property: PropertyEntity) {
        propertyRepository.add(property)
    }
}