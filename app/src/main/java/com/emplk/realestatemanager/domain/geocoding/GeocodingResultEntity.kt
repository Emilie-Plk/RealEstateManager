package com.emplk.realestatemanager.domain.geocoding

data class GeocodingResultEntity(
    val placeId: String,
    val geometry: GeometryEntity,
    val formattedAddress: String, // not even sure I need it
)