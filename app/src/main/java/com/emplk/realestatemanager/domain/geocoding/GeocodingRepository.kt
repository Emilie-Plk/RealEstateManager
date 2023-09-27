package com.emplk.realestatemanager.domain.geocoding

interface GeocodingRepository {
    suspend fun getLatLong(address: String): GeocodingWrapper
}
