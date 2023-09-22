package com.emplk.realestatemanager.domain.property_form

import javax.inject.Inject

class DeleteTemporaryPropertyFormUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository
) {
    suspend fun invoke(propertyFormId: Long) = propertyFormRepository.delete(propertyFormId)
}