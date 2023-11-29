package com.emplk.realestatemanager.domain.property_draft.save_draft

import javax.inject.Inject

class ResetSaveDraftStateUseCase @Inject constructor(
    private val saveDraftStateRepository: SaveDraftStateRepository
) {
    fun invoke() {
        saveDraftStateRepository.resetSaveDraftState()
    }
}