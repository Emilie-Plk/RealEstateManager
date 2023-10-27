package com.emplk.realestatemanager.domain.property_draft

import android.util.Log
import com.emplk.realestatemanager.ui.add.FormType
import javax.inject.Inject

class InitPropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val addPropertyFormWithDetailsUseCase: AddPropertyFormWithDetailsUseCase,
) {
    suspend fun invoke(id: Long?): FormWithTypeEntity {
        return if (id == null || id == 0L) {
            val newlyAddedFormId = addPropertyFormWithDetailsUseCase.invoke(null)
            Log.d("COUCOU", "invoke: Newly added form id: $newlyAddedFormId")
            FormWithTypeEntity(formDraftRepository.getPropertyFormById(newlyAddedFormId), FormType.ADD) // cas add new
        } else {
            val doesDraftExist = formDraftRepository.doesDraftExist(id)
            val doesPropertyExist = formDraftRepository.doesPropertyExist(id)
            if (doesDraftExist && !doesPropertyExist) { // cas add draft existant
                Log.d("COUCOU", "invoke: Draft exist but not property: $id ADD")
                FormWithTypeEntity(formDraftRepository.getPropertyFormById(id), FormType.ADD)
            } else if (doesPropertyExist && doesDraftExist) { // cas edit draft
                // case edit draft existant
                Log.d("COUCOU", "invoke: Draft exist and property: $id EDIT")
                FormWithTypeEntity(formDraftRepository.getPropertyFormById(id), FormType.EDIT)
            } else if (doesPropertyExist) { //  cas edit new property
                // case new add draft
                addPropertyFormWithDetailsUseCase.invoke(id)
                FormWithTypeEntity(formDraftRepository.getPropertyFormById(id), FormType.EDIT)
            } else throw Exception("Error in InitPropertyFormUseCase")
        }
    }
}

