package com.emplk.realestatemanager.domain.property_draft.address

import com.emplk.realestatemanager.data.property_draft.address.PredictionAddressState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPredictionAddressStateFlowUseCase @Inject constructor(
    private val predictionAddressStateRepository: PredictionAddressStateRepository
) {
    fun invoke(): Flow<PredictionAddressState?> = predictionAddressStateRepository.getPredictionAddressStateAsFlow()
}