package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class DeleteTemporaryPropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val getCurrentDraftIdUseCase: GetCurrentDraftIdUseCase,
) {
    suspend fun invoke(): Boolean {
        val currentPropertyFormId = getCurrentDraftIdUseCase.invoke()
        return if (currentPropertyFormId != null) {
            formDraftRepository.delete(currentPropertyFormId)
        } else {
            false
        }
    }
}