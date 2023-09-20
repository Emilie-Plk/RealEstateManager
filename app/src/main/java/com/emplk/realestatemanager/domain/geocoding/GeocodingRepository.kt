package com.emplk.realestatemanager.domain.geocoding

interface GeocodingRepository {
    suspend fun getLatLong(placeId: String): GeocodingWrapper
}
