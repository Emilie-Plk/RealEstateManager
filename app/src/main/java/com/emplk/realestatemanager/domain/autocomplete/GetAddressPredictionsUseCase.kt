package com.emplk.realestatemanager.domain.autocomplete

import javax.inject.Inject

class GetAddressPredictionsUseCase @Inject constructor(
    private val predictionRepository: PredictionRepository
) {
    suspend fun invoke(query: String): PredictionWrapper = predictionRepository.getAddressPredictions(query)
}
