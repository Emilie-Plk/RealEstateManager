package com.emplk.realestatemanager.data.autocomplete

import android.util.LruCache
import com.emplk.realestatemanager.data.api.GoogleApi
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.autocomplete.PredictionEntity
import com.emplk.realestatemanager.domain.autocomplete.PredictionRepository
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PredictionRepositoryAutocomplete @Inject constructor(
    private val googleApi: GoogleApi,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PredictionRepository {

    private companion object {
        private const val TYPE = "address"
    }

    private val predictionsLruCache = LruCache<String, List<PredictionEntity>>(200)

    override suspend fun getPredictions(query: String): List<PredictionEntity> =
        withContext(coroutineDispatcherProvider.io) {
            predictionsLruCache.get(query) ?: try {
                val response = googleApi.getAddressPredictions(query, TYPE)

                when (response.status) {
                    "OK" -> response.predictions?.mapNotNull { predictionResponse ->
                        if (predictionResponse.placeId == null || predictionResponse.description == null) {
                            return@mapNotNull null
                        }
                        PredictionEntity(
                            predictionResponse.placeId,
                            predictionResponse.description
                        )
                    }.also {
                        predictionsLruCache.put(query, it)
                    } ?: emptyList()


                    "ZERO_RESULTS" -> emptyList()

                    else -> emptyList()
                }
            } catch (e: Exception) {
                coroutineContext.ensureActive()
                emptyList()
            }
        }
}