package com.emplk.realestatemanager.data.property_draft.save_draft

import com.emplk.realestatemanager.domain.property_draft.save_draft.SaveDraftStateRepository
import com.emplk.realestatemanager.ui.add.FormType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

class SaveDraftStateRepositoryImpl @Inject constructor() : SaveDraftStateRepository {
    private val saveDraftStateMutableStateFlow: MutableStateFlow<SaveDraftState?> = MutableStateFlow(null)

    override fun setSaveDraftState(saveDraftState: SaveDraftState) {
        saveDraftStateMutableStateFlow.tryEmit(saveDraftState)
    }

    override fun getSaveDraftState(): Flow<SaveDraftState> {
        return saveDraftStateMutableStateFlow.filterNotNull()
    }

    override fun resetSaveDraftState() {
        saveDraftStateMutableStateFlow.tryEmit(null)
    }
}

data class SaveDraftState(
    val formType: FormType,
    val formTitle: String?,
)