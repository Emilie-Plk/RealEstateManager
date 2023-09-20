package com.emplk.realestatemanager.domain.geocoding

import com.emplk.realestatemanager.domain.location.LocationEntity

data class GeometryEntity(
    val lat: String,
    val lng: String,
    val locationType: String?,
)
