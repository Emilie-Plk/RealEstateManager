package com.emplk.realestatemanager.data.navigation

import com.emplk.realestatemanager.domain.navigation.draft.FormRepository
import com.emplk.realestatemanager.domain.property_draft.model.FormTypeAndTitleEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class FormRepositoryImpl @Inject constructor() : FormRepository {
    private val formParamsEntity = FormParamsEntity()

    override fun savePropertyDraftEvent() {
        formParamsEntity.savePropertyMutableSharedFlow.tryEmit(Unit)
    }

    override fun getSavePropertyDraftEvent(): Flow<Unit> = formParamsEntity.savePropertyMutableSharedFlow

    override fun clearPropertyDraftEvent() {
        formParamsEntity.clearPropertyMutableSharedFlow.tryEmit(Unit)
    }

    override fun getClearedPropertyDraftEvent(): Flow<Unit> = formParamsEntity.clearPropertyMutableSharedFlow

    override fun setPropertyFormProgress(isPropertyFormInProgress: Boolean) {
        formParamsEntity.isFormInProgressMutableStateFlow.tryEmit(isPropertyFormInProgress)
    }

    override fun isPropertyFormInProgressAsFlow(): Flow<Boolean?> = formParamsEntity.isFormInProgressMutableStateFlow

    override fun resetPropertyFormProgress() {
        formParamsEntity.isFormInProgressMutableStateFlow.tryEmit(null)
    }

    override fun setPropertyFormCompletion(isPropertyFormCompleted: Boolean) {
        formParamsEntity.isFormCompletedMutableStateFlow.tryEmit(isPropertyFormCompleted)
    }

    override fun isPropertyFormCompletedAsFlow(): Flow<Boolean?> = formParamsEntity.isFormCompletedMutableStateFlow

    override fun resetPropertyFormCompletion() {
        formParamsEntity.isFormCompletedMutableStateFlow.tryEmit(null)
    }

    override fun setPropertyAddedInDatabase(isPropertyAddedInDB: Boolean) {
        formParamsEntity.isSavingPropertyInDatabaseMutableSharedFlow.tryEmit(isPropertyAddedInDB)
    }

    override fun isPropertyAddedInDatabaseAsFlow(): Flow<Boolean?> =
        formParamsEntity.isSavingPropertyInDatabaseMutableSharedFlow

    override fun setFormTypeAndTitle(formTypeAndDraftTitle: FormTypeAndTitleEntity) {
        formParamsEntity.formTypeAndTitleMutableStateFlow.tryEmit(formTypeAndDraftTitle)
    }

    override fun getFormTypeAndTitleAsFlow(): Flow<FormTypeAndTitleEntity?> =
        formParamsEntity.formTypeAndTitleMutableStateFlow

    override fun resetFormTypeAndTitle() {
        formParamsEntity.formTypeAndTitleMutableStateFlow.tryEmit(null)
    }
}

data class FormParamsEntity(
    val savePropertyMutableSharedFlow: MutableSharedFlow<Unit> = MutableSharedFlow(extraBufferCapacity = 1),
    val clearPropertyMutableSharedFlow: MutableSharedFlow<Unit> = MutableSharedFlow(extraBufferCapacity = 1),
    val isSavingPropertyInDatabaseMutableSharedFlow: MutableSharedFlow<Boolean> = MutableSharedFlow(extraBufferCapacity = 1),
    val isFormInProgressMutableStateFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null),
    val isFormCompletedMutableStateFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null),
    val formTypeAndTitleMutableStateFlow: MutableStateFlow<FormTypeAndTitleEntity?> = MutableStateFlow(null),
)
