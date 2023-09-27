package com.emplk.realestatemanager.domain.autocomplete

interface PredictionRepository {

    suspend fun getAddressPredictions(query: String): PredictionWrapper
}
