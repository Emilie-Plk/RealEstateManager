package com.emplk.realestatemanager.domain.navigation.draft

import kotlinx.coroutines.flow.Flow

interface NavigationDraftRepository {
    fun savePropertyDraftEvent()
    fun getSavedPropertyDraftEvent(): Flow<Unit>
}
