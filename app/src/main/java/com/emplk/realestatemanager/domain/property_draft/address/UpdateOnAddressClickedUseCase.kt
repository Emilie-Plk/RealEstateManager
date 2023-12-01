package com.emplk.realestatemanager.domain.property_draft.address

import com.emplk.realestatemanager.domain.property_draft.FormDraftRepository
import javax.inject.Inject

class UpdateOnAddressClickedUseCase @Inject constructor(
    private val predictionAddressStateRepository: PredictionAddressStateRepository,
    private val formDraftRepository: FormDraftRepository,
) {
    suspend fun invoke(isAddressSelected: Boolean, id: Long) {
        predictionAddressStateRepository.setIsPredictionSelectedByUser(isAddressSelected)
        formDraftRepository.updateAddressValidity(id, isAddressSelected)
    }
}
