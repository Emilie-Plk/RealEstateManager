package com.emplk.realestatemanager.domain.property_form

import javax.inject.Inject

class UpdatePropertyFormUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository
) {
    suspend fun invoke(propertyFormEntity: PropertyFormEntity) {
        propertyFormRepository.update(propertyFormEntity)
    }
}