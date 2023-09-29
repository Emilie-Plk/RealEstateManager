package com.emplk.realestatemanager.domain.property.location

import com.google.android.gms.maps.model.LatLng

data class LocationEntity(
    val address: String,
    val miniatureMapPath: String?,
    val latLng: LatLng?,
)