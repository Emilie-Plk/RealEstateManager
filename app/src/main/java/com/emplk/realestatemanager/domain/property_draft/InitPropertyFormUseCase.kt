package com.emplk.realestatemanager.domain.property_draft

import android.util.Log
import com.emplk.realestatemanager.ui.add.FormType
import javax.inject.Inject

/**
 * Initialize the form with the correct type (ADD or EDIT) with new or existing form
 * @param id: Long? - null if adding new form
 * @return FormWithTypeEntity
 */
class InitPropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val addPropertyFormWithDetailsUseCase: AddPropertyFormWithDetailsUseCase,
) {
    suspend fun invoke(id: Long?): FormWithTypeEntity {
        return if (id == null || id == 0L) {
            val existingEmptyFormId = formDraftRepository.getEmptyFormId()
            if (existingEmptyFormId != null) {
                // case add empty form already exists
                FormWithTypeEntity(
                    formDraftRepository.getFormDraftEntityById(existingEmptyFormId),
                    FormType.ADD
                )
            } else {
                // case add new draft
                val newlyAddedFormId = addPropertyFormWithDetailsUseCase.invoke(null)
                Log.d("InitPropertyFormUseCase", "invoke: Newly added form id: $newlyAddedFormId")
                FormWithTypeEntity(
                    formDraftRepository.getFormDraftEntityById(newlyAddedFormId),
                    FormType.ADD
                )
            }
        } else {
            val doesDraftExist = formDraftRepository.doesFormExist(id)
            val doesPropertyExist = formDraftRepository.doesPropertyExist(id)
            if (doesDraftExist && !doesPropertyExist) {
                // case "add draft" already exists
                Log.d("InitPropertyFormUseCase", "invoke: Draft exist but not property: $id ADD")
                FormWithTypeEntity(formDraftRepository.getFormDraftEntityById(id), FormType.ADD)
            } else if (doesPropertyExist && doesDraftExist) {
                // case edit draft already exists
                Log.d("InitPropertyFormUseCase", "invoke: Draft exist and property: $id EDIT")
                FormWithTypeEntity(formDraftRepository.getFormDraftEntityById(id), FormType.EDIT)
            } else if (doesPropertyExist) {
                //  case edit new draft
                addPropertyFormWithDetailsUseCase.invoke(id)
                FormWithTypeEntity(formDraftRepository.getFormDraftEntityById(id), FormType.EDIT)
            } else throw Exception("Error in InitPropertyFormUseCase with id: $id")
        }
    }
}

