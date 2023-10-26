package com.emplk.realestatemanager.data.navigation

import com.emplk.realestatemanager.domain.navigation.draft.NavigationDraftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class NavigationDraftRepositoryImpl @Inject constructor() : NavigationDraftRepository {
    private val savePropertyDraftMutableSharedFlow: MutableSharedFlow<Unit> = MutableSharedFlow(extraBufferCapacity = 1)
    private val clearPropertyDraftMutableSharedFlow: MutableSharedFlow<Unit> =
        MutableSharedFlow(extraBufferCapacity = 1)
    private val isPropertyFormInProgressMutableStateFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    override fun savePropertyDraftEvent() {
        savePropertyDraftMutableSharedFlow.tryEmit(Unit)
    }

    override fun getSavedPropertyDraftEvent(): Flow<Unit> = savePropertyDraftMutableSharedFlow

    override fun clearPropertyDraftEvent() {
        clearPropertyDraftMutableSharedFlow.tryEmit(Unit)
    }

    override fun getClearedPropertyDraftEvent(): Flow<Unit> = clearPropertyDraftMutableSharedFlow

    override fun setPropertyFormProgress(isPropertyFormInProgress: Boolean) {
        isPropertyFormInProgressMutableStateFlow.tryEmit(isPropertyFormInProgress)
    }

    override fun isPropertyFormInProgressAsFlow(): Flow<Boolean?> =
        isPropertyFormInProgressMutableStateFlow

    override fun clearPropertyFormProgress() {
        isPropertyFormInProgressMutableStateFlow.tryEmit(false)
    }
}