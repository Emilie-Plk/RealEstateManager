package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class InitAddOrEditPropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val addPropertyFormWithDetailsUseCase: AddPropertyFormWithDetailsUseCase,
) {
    suspend fun invoke(id: Long?): PropertyFormDatabaseState {
        val doesPropertyDraftExist = formDraftRepository.doesPropertyDraftExist(id)
        val doesPropertyExistInBothTables = formDraftRepository.doesPropertyExistInBothTables(id)
        if (id == null) {
            val existingAddDraftId = formDraftRepository.getAddFormId()
            return if (existingAddDraftId == null) {
                // case add new
                PropertyFormDatabaseState.EmptyForm(addPropertyFormWithDetailsUseCase.invoke(null))
            } else {
                // case existing add
                PropertyFormDatabaseState.Draft(
                    formDraftRepository.getPropertyFormById(
                        existingAddDraftId
                    )
                )
            }
        } else {
            return if (doesPropertyExistInBothTables) { //  s'il existe un draft + une property
                // case edit draft existant
                PropertyFormDatabaseState.Draft(formDraftRepository.getPropertyFormById(id))
            } else if (!doesPropertyDraftExist) { // s'il n'existe pas de draft
                // case new edit draft
                addPropertyFormWithDetailsUseCase.invoke(id)
                PropertyFormDatabaseState.Draft(formDraftRepository.getPropertyFormById(id))
            } else throw IllegalStateException("Property draft exist but not in both tables")
        }
    }
}

