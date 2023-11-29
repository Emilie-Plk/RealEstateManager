package com.emplk.realestatemanager.domain.property_draft.save_draft

import com.emplk.realestatemanager.data.property_draft.save_draft.SaveDraftState
import javax.inject.Inject

class SetSaveDraftStateUseCase @Inject constructor(
    private val saveDraftStateRepository: SaveDraftStateRepository
) {
    fun invoke(saveDraftState: SaveDraftState) {
        saveDraftStateRepository.setSaveDraftState(saveDraftState)
    }
}