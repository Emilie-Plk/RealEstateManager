package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.ui.add.FormType
import javax.inject.Inject

class InitPropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val addPropertyFormWithDetailsUseCase: AddPropertyFormWithDetailsUseCase,
) {
    suspend fun invoke(id: Long?): FormState {
        return if (id == null || id == 0L) {
            FormState.EmptyForm(addPropertyFormWithDetailsUseCase.invoke(null), FormType.ADD)
        } else {
            val doesDraftExist = formDraftRepository.doesDraftExist(id)
            val doesPropertyExist = formDraftRepository.doesPropertyExist(id)
            if (doesDraftExist && !doesPropertyExist) {
                FormState.Draft(formDraftRepository.getPropertyFormById(id), FormType.ADD) // s'il existe un add draft
            }
            if (doesPropertyExist && doesDraftExist) { //  s'il existe un draft + une property
                // case add/edit draft existant
                FormState.Draft(formDraftRepository.getPropertyFormById(id), FormType.EDIT)
            } else if (!doesPropertyExist && doesDraftExist) {
                // case new add draft
                FormState.EmptyForm(addPropertyFormWithDetailsUseCase.invoke(null), FormType.ADD)
            }else  { // s'il n'existe pas de draft
                // case new edit draft
                addPropertyFormWithDetailsUseCase.invoke(id)
                FormState.Draft(formDraftRepository.getPropertyFormById(id), FormType.EDIT)
            }
        }
    }
}

