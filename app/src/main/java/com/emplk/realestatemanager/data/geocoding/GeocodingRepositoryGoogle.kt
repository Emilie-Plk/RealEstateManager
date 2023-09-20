package com.emplk.realestatemanager.data.geocoding

import com.emplk.realestatemanager.data.api.GoogleApi
import com.emplk.realestatemanager.data.geocoding.response.GeocodingResponse
import com.emplk.realestatemanager.data.geocoding.response.GeometryResponse
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.geocoding.GeocodingRepository
import com.emplk.realestatemanager.domain.geocoding.GeocodingResultEntity
import com.emplk.realestatemanager.domain.geocoding.GeocodingWrapper
import com.emplk.realestatemanager.domain.geocoding.GeometryEntity
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GeocodingRepositoryGoogle @Inject constructor(
    private val googleApi: GoogleApi,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : GeocodingRepository {
    override suspend fun getLatLong(placeId: String): GeocodingWrapper = withContext(coroutineDispatcherProvider.io) {
        try {
            val response = googleApi.getGeocode(placeId)
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

    private fun mapResults(response: GeocodingResponse?): List<GeocodingResultEntity>? {
        val responseResult = response?.results?.firstOrNull() ?: return null

        val placeId = responseResult.placeId
        val geometry = mapGeometry(responseResult.geometry)
        val formattedAddress = responseResult.formattedAddress

        if (placeId != null && geometry != null && formattedAddress != null) {
            return listOf(GeocodingResultEntity(placeId, geometry, formattedAddress))
        }
        return null
    }

    private fun mapGeometry(geometryResponse: GeometryResponse?): GeometryEntity? {
        if (geometryResponse?.location?.lat == null ||
            geometryResponse.location.lng == null ||
            geometryResponse.locationType == null
        ) {
            return null
        }
        return GeometryEntity(
            geometryResponse.location.lat,
            geometryResponse.location.lng,
            geometryResponse.locationType
        )
    }
}