package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class InitPropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val addPropertyFormWithDetailsUseCase: AddPropertyFormWithDetailsUseCase,
) {
    suspend fun invoke(id: Long?): PropertyFormDatabaseState =
        if (id == null) {
            val existingAddDraftId = formDraftRepository.getAddFormId()
            if (existingAddDraftId == null) {
                // case add new
                PropertyFormDatabaseState.EmptyForm(addPropertyFormWithDetailsUseCase.invoke(null))
            } else {
                // case existing add
                PropertyFormDatabaseState.Draft(
                    formDraftRepository.getPropertyFormById(existingAddDraftId)
                )
            }
        } else {
            invoke(id)
        }

    suspend fun invoke(id: Long): PropertyFormDatabaseState.Draft {
        val doesPropertyDraftExist = formDraftRepository.doesPropertyDraftExist(id)
        val doesPropertyExist = formDraftRepository.doesPropertyExist(id)

        return if (doesPropertyDraftExist && doesPropertyExist) { //  s'il existe un draft + une property
            // case edit draft existant
            PropertyFormDatabaseState.Draft(formDraftRepository.getPropertyFormById(id))
        } else if (!doesPropertyDraftExist) { // s'il n'existe pas de draft
            // case new edit draft
            addPropertyFormWithDetailsUseCase.invoke(id)
            PropertyFormDatabaseState.Draft(formDraftRepository.getPropertyFormById(id))
        } else {
            throw IllegalStateException("Property draft with id $id exists but its property doesn't")
        }
    }
}

