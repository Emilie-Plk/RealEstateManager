package com.emplk.realestatemanager.domain.autocomplete

import javax.inject.Inject

class GetAddressPredictionsUseCase @Inject constructor(
    private val predictionRepository: PredictionRepository
) {
    suspend fun invoke(query: String): List<PredictionEntity> = predictionRepository.getPredictions(query)
}
