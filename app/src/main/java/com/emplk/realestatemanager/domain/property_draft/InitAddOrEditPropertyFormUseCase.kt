package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class InitAddOrEditPropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val addTemporaryPropertyFormUseCase: AddTemporaryPropertyFormUseCase,
) {
    suspend fun invoke(id: Long?): PropertyFormDatabaseState {
        val doesPropertyDraftExist = formDraftRepository.doesPropertyDraftExist(id)
        val doesPropertyExistInBothTables = formDraftRepository.doesPropertyExistInBothTables(id)
        if (id == null) {
            val existingAddDraftId = formDraftRepository.getAddFormId()
            return if (existingAddDraftId == null) {
                // case add new
                PropertyFormDatabaseState.EmptyForm(addTemporaryPropertyFormUseCase.invoke(null))
            } else {
                // case existing add
                PropertyFormDatabaseState.Draft(
                    formDraftRepository.getPropertyFormById(
                        existingAddDraftId
                    )
                )
            }
        } else {
            return if (doesPropertyExistInBothTables) { //  s'il existe un draft et dans 2 tables
                // case edit draft existant
                PropertyFormDatabaseState.Draft(formDraftRepository.getPropertyFormById(id))
            } else if (!doesPropertyDraftExist) { // s'il n'existe pas de draft
                // case new edit draft  IL MAPPER OK MAIS INSERT AUSSI
                PropertyFormDatabaseState.EmptyForm(addTemporaryPropertyFormUseCase.invoke(id))
            } else throw IllegalStateException("Property draft exist but not in both tables")
        }
    }
}

