package com.emplk.realestatemanager.domain.autocomplete

interface PredictionRepository {

    suspend fun getPredictions(query: String): List<PredictionEntity>
}
