package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class InitAddOrEditPropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val addTemporaryPropertyFormUseCase: AddTemporaryPropertyFormUseCase,
) {

    suspend fun invoke(id: Long?): PropertyFormDatabaseState {
        return if (id == null) {
            // case add new
            PropertyFormDatabaseState.Empty(addTemporaryPropertyFormUseCase.invoke(null))
        } else {
            val doesPropertyDraftExist = formDraftRepository.doesPropertyDraftExist(id)
            val doesPropertyExistInBothTables = formDraftRepository.doesPropertyExistInBothTables(id)
            if (doesPropertyDraftExist && !doesPropertyExistInBothTables) { // s'il existe un draft mais pas de property...
                val existingAddDraftId = formDraftRepository.getAddFormId()
                if (existingAddDraftId != null) {
                    // case add existant
                    PropertyFormDatabaseState.DraftAlreadyExists(
                        formDraftRepository.getPropertyFormById(existingAddDraftId)
                    )
                } else {
                    // if id null, we create a new draft
                    PropertyFormDatabaseState.Empty(addTemporaryPropertyFormUseCase.invoke(null))
                }
            } else if (doesPropertyDraftExist) { // s'il existe un draft et dans 2 tables
                // case edit existant
                return PropertyFormDatabaseState.DraftAlreadyExists(formDraftRepository.getPropertyFormById(id))
            } else {
                // case new edit
                return PropertyFormDatabaseState.Empty(addTemporaryPropertyFormUseCase.invoke(id))
            }
        }
    }
}
