package com.emplk.realestatemanager.domain.property_draft.save_draft

import com.emplk.realestatemanager.data.property_draft.save_draft.SaveDraftState
import kotlinx.coroutines.flow.Flow

interface SaveDraftStateRepository {
    fun setSaveDraftState(saveDraftState: SaveDraftState)
    fun getSaveDraftState(): Flow<SaveDraftState>
    fun resetSaveDraftState()
}
