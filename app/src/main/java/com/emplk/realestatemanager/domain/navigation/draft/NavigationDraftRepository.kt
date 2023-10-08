package com.emplk.realestatemanager.domain.navigation.draft

import kotlinx.coroutines.flow.Flow

interface NavigationDraftRepository {
    fun savePropertyDraftEvent()
    fun getSavedPropertyDraftEvent(): Flow<Unit>
    fun setPropertyFormProgress(isPropertyFormInProgress: Boolean)
    fun isPropertyFormInProgressAsFlow(): Flow<Boolean>
}
