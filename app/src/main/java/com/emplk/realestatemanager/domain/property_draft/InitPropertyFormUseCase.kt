package com.emplk.realestatemanager.domain.property_draft

import javax.inject.Inject

class InitPropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val addPropertyFormWithDetailsUseCase: AddPropertyFormWithDetailsUseCase,
) {
    suspend fun invoke(id: Long?): FormState {
        return if (id == null || id == 0L) {
            FormState.EmptyForm(addPropertyFormWithDetailsUseCase.invoke(null))
        } else {
            val doesDraftExist = formDraftRepository.doesDraftExist(id)
            val doesPropertyExist = formDraftRepository.doesPropertyExist(id)
            if (doesDraftExist && !doesPropertyExist) {
                FormState.Draft(formDraftRepository.getPropertyFormById(id)) // s'il existe un add draft
            }
            if (doesPropertyExist && doesDraftExist || !doesPropertyExist && doesDraftExist) { //  s'il existe un draft + une property
                // case add/edit draft existant
                FormState.Draft(formDraftRepository.getPropertyFormById(id))
            } else  { // s'il n'existe pas de draft
                // case new edit draft
                addPropertyFormWithDetailsUseCase.invoke(id)
                FormState.Draft(formDraftRepository.getPropertyFormById(id))
            }
        }
    }
}

