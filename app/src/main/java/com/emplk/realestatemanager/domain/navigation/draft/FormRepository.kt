package com.emplk.realestatemanager.domain.navigation.draft

import com.emplk.realestatemanager.domain.property_draft.model.FormTypeAndTitleEntity
import kotlinx.coroutines.flow.Flow

interface FormRepository {
    // PropertyDraft save Event
    fun savePropertyDraftEvent()
    fun getSavePropertyDraftEvent(): Flow<Unit>

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
    fun resetPropertyFormCompletion()

    // Property Added in DB
    fun setPropertyAddedInDatabase(isPropertyAddedInDB: Boolean)
    fun isPropertyAddedInDatabaseAsFlow(): Flow<Boolean?>

    // FormTypeAndTitle
    fun setFormTypeAndTitle(formTypeAndDraftTitle: FormTypeAndTitleEntity)
    fun getFormTypeAndTitleAsFlow(): Flow<FormTypeAndTitleEntity?>
    fun resetFormTypeAndTitle()
}