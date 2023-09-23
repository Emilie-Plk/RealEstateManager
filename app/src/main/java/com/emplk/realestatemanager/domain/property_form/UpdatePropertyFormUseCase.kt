package com.emplk.realestatemanager.domain.property_form

import javax.inject.Inject

class UpdatePropertyFormUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository,
    private val getCurrentPropertyFormIdUseCase: GetCurrentPropertyFormIdUseCase,
) {
    suspend fun invoke(propertyFormEntity: PropertyFormEntity) {
        propertyFormRepository.update(propertyFormEntity, getCurrentPropertyFormIdUseCase.invoke())
    }
}