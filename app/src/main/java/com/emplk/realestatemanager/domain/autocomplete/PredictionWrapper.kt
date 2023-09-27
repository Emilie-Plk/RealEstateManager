package com.emplk.realestatemanager.domain.autocomplete

sealed class PredictionWrapper {
    data class Success(val predictions: List<String>) : PredictionWrapper()
    object NoResult : PredictionWrapper()
    data class Error(val error: String) : PredictionWrapper()
    data class Failure(val failure: Throwable) : PredictionWrapper()
}