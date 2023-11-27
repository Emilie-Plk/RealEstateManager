package com.emplk.realestatemanager.domain.property_draft.address

import javax.inject.Inject

class ResetSelectedAddressStateUseCase @Inject constructor(
    private val predictionAddressStateRepository: PredictionAddressStateRepository,
) {
    fun invoke() { predictionAddressStateRepository.resetSelectedAddressState() }
}