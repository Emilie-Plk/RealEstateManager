package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class InitPropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val addPropertyFormWithDetailsUseCase: AddPropertyFormWithDetailsUseCase,
) {
    suspend fun invoke(id: Long?): FormState {
        if (id == null || id == 0L) {
            val existingAddDraftId = formDraftRepository.getAddFormId()
            return if (existingAddDraftId == null) {
                // case add new
                FormState.EmptyForm(addPropertyFormWithDetailsUseCase.invoke(null))
            } else {
                // case existing add
                FormState.Draft(formDraftRepository.getPropertyFormById(existingAddDraftId))
            }
        } else {
            val doesDraftExist = formDraftRepository.doesDraftExist(id)
            val doesPropertyExist = formDraftRepository.doesPropertyExist(id)
            return if (doesPropertyExist && doesDraftExist) { //  s'il existe un draft + une property
                // case edit draft existant
                FormState.Draft(formDraftRepository.getPropertyFormById(id))
            } else if (!doesDraftExist) { // s'il n'existe pas de draft
                // case new edit draft
                addPropertyFormWithDetailsUseCase.invoke(id)
                FormState.Draft(formDraftRepository.getPropertyFormById(id))
            } else throw IllegalStateException("Property draft with id $id exist but not in properties table")
        }
    }
}

