package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class GetCurrentPropertyDraftIdUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository
) {
    suspend fun invoke(): Long? = propertyFormRepository.getExistingPropertyFormId()
}