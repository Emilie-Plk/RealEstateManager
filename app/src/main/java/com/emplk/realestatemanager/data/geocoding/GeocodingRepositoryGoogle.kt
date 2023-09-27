package com.emplk.realestatemanager.data.geocoding

import com.emplk.realestatemanager.data.api.GoogleApi
import com.emplk.realestatemanager.data.geocoding.response.GeocodingResponse
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.geocoding.GeocodingRepository
import com.emplk.realestatemanager.domain.geocoding.GeocodingResultEntity
import com.emplk.realestatemanager.domain.geocoding.GeocodingWrapper
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GeocodingRepositoryGoogle @Inject constructor(
    private val googleApi: GoogleApi,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : GeocodingRepository {
    override suspend fun getLatLong(address: String): GeocodingWrapper = withContext(coroutineDispatcherProvider.io) {
        try {
            val response = googleApi.getGeocode(address)
            when (response.status) {
                "OK" -> {
                    val result = mapResults(response)

                    if (result != null) {
                        GeocodingWrapper.Success(result)
                    } else {
                        GeocodingWrapper.Error("Status OK but error while trying to get Geocoding results")
                    }
                }

                "ZERO_RESULTS" -> {
                    GeocodingWrapper.NoResult
                }

                else -> GeocodingWrapper.Error(
                    response.status ?: "Unknown error occurred while trying to get Geocoding results"
                )
            }

        } catch (e: Exception) {
            coroutineContext.ensureActive()
            GeocodingWrapper.Error(e.message ?: "Unknown error occurred while trying to get Geocoding results")
        }
    }

    private fun mapResults(response: GeocodingResponse?): GeocodingResultEntity? {
        val responseResult = response?.results?.firstOrNull() ?: return null
        val lat = responseResult.geometry?.location?.lat
        val lng = responseResult.geometry?.location?.lng

        if (lat != null && lng != null) {
            return GeocodingResultEntity(lat, lng)
        }
        return null
    }
}