package com.emplk.realestatemanager.domain.geocoding

data class GeometryEntity(
    val lat: String,
    val lng: String,
    val locationType: String?,
)
