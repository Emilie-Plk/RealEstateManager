package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class DeleteTemporaryPropertyFormUseCase @Inject constructor(
    private val propertyFormRepository: PropertyFormRepository,
    private val getCurrentPropertyDraftIdUseCase: GetCurrentPropertyDraftIdUseCase,
) {
    suspend fun invoke(): Boolean {
        val currentPropertyFormId = getCurrentPropertyDraftIdUseCase.invoke()
        return if (currentPropertyFormId != null) {
            propertyFormRepository.delete(currentPropertyFormId)
        } else {
            false
        }
    }
}