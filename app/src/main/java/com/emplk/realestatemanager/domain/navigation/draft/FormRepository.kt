package com.emplk.realestatemanager.domain.navigation.draft

import com.emplk.realestatemanager.data.property_draft.FormTypeAndTitleEntity
import kotlinx.coroutines.flow.Flow

interface FormRepository {
    // PropertyDraft save Event
    fun savePropertyDraftEvent()
    fun getSavedPropertyDraftEvent(): Flow<Unit>

    // PropertyDraft clear Event
    fun clearPropertyDraftEvent()
    fun getClearedPropertyDraftEvent(): Flow<Unit>

    // PropertyDraft progress
    fun setPropertyFormProgress(isPropertyFormInProgress: Boolean)
    fun isPropertyFormInProgressAsFlow(): Flow<Boolean?>
    fun resetPropertyFormProgress()

    // PropertyDraft completion
    fun setPropertyFormCompletion(isPropertyFormCompleted: Boolean)
    fun isPropertyFormCompletedAsFlow(): Flow<Boolean?>
    fun resetPropertyFormCompletion() // TODO: use it where it needs to be

    // FormTypeAndTitle
    fun setFormTypeAndTitle(formTypeAndDraftTitle: FormTypeAndTitleEntity)
    fun getFormTypeAndTitleAsFlow(): Flow<FormTypeAndTitleEntity>
    fun resetFormTypeAndTitle()
}