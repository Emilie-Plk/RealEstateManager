package com.emplk.realestatemanager.domain.property_draft.save_draft

import com.emplk.realestatemanager.data.property_draft.save_draft.SaveDraftState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSaveDraftStateFlowUseCase @Inject constructor(
    private val saveDraftStateRepository: SaveDraftStateRepository
) {
    fun invoke(): Flow<SaveDraftState> = saveDraftStateRepository.getSaveDraftState()
}