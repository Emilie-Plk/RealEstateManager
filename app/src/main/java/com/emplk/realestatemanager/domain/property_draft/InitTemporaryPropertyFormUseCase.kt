package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class InitTemporaryPropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val addTemporaryPropertyFormUseCase: AddTemporaryPropertyFormUseCase,
) {

    suspend fun invoke(): PropertyFormDatabaseState {
        val existingPropertyFormEntity = formDraftRepository.getExistingPropertyForm()
        return if (existingPropertyFormEntity != null) {
            PropertyFormDatabaseState.DraftAlreadyExists(existingPropertyFormEntity)
        } else {
            PropertyFormDatabaseState.Empty(addTemporaryPropertyFormUseCase.invoke())
        }
    }
}