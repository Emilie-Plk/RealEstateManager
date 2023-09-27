package com.emplk.realestatemanager.domain.geocoding

sealed class GeocodingWrapper {
    data class Success(val result: GeocodingResultEntity) : GeocodingWrapper()
    object NoResult : GeocodingWrapper()
    data class Error(val error: String) : GeocodingWrapper()
}
