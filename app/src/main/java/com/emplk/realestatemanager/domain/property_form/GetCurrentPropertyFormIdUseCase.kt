package com.emplk.realestatemanager.domain.property_form

import javax.inject.Inject

class GetCurrentPropertyFormIdUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository
) {
    suspend fun invoke(): Long = propertyFormRepository.getExistingPropertyFormId()
}