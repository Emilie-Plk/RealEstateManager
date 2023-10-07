package com.emplk.realestatemanager.data.navigation

import com.emplk.realestatemanager.domain.navigation.draft.NavigationDraftRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

class NavigationDraftRepositoryImpl @Inject constructor() : NavigationDraftRepository {
    private val savePropertyDraftMutableSharedFlow: MutableSharedFlow<Unit> = MutableSharedFlow(extraBufferCapacity = 1)
    override fun savePropertyDraftEvent() {
        savePropertyDraftMutableSharedFlow.tryEmit(Unit)
    }

    override fun getSavedPropertyDraftEvent(): Flow<Unit> = savePropertyDraftMutableSharedFlow
}