package com.emplk.realestatemanager.domain.autocomplete

import javax.inject.Inject

class GetAddressPredictionsUseCase @Inject constructor(
    private val predictionRepository: PredictionRepository
) {
    suspend fun invoke(query: String): List<PredictionEntity> {
        return if (query.length >= 3) predictionRepository.getPredictions(query)
        else emptyList()
    }
}
