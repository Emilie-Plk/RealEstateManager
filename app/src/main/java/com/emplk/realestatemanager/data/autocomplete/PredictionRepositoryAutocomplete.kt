package com.emplk.realestatemanager.data.autocomplete

import android.util.LruCache
import com.emplk.realestatemanager.data.DataModule
import com.emplk.realestatemanager.data.api.GoogleApi
import com.emplk.realestatemanager.data.autocomplete.response.AutocompleteResponse
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.autocomplete.PredictionRepository
import com.emplk.realestatemanager.domain.autocomplete.PredictionWrapper
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PredictionRepositoryAutocomplete @Inject constructor(
    private val googleApi: GoogleApi,
    @DataModule.LruCachePredictions private val predictionsLruCache: LruCache<String, PredictionWrapper>,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PredictionRepository {

    private companion object {
        private const val TYPE = "address"
    }

    override suspend fun getAddressPredictions(query: String): PredictionWrapper =
        withContext(coroutineDispatcherProvider.io) {
            predictionsLruCache.get(query) ?: try {  //TODO: check if this is correct
                val response = googleApi.getAddressPredictions(query, TYPE)
                when (response.status) {
                    "OK" -> {
                        val predictions = mapResponseToPredictionWrapper(response)
                        if (predictions.isEmpty()) PredictionWrapper.NoResult
                        else PredictionWrapper.Success(predictions)
                    }

                    "ZERO_RESULTS" -> PredictionWrapper.NoResult
                    else -> PredictionWrapper.Failure(Throwable(response.status))
                }.also { predictionsLruCache.put(query, it) }
            } catch (e: Exception) {
                e.printStackTrace()
                PredictionWrapper.Error(e.message ?: "Unknown error occurred")
            }
        }

    private fun mapResponseToPredictionWrapper(response: AutocompleteResponse): List<String> =
        response.predictions?.mapNotNull { prediction ->
            if (prediction.description.isNullOrBlank()) return@mapNotNull null
            else prediction.description
        } ?: emptyList()
}