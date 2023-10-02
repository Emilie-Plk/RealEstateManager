package com.emplk.realestatemanager.domain.property.location

import com.google.android.gms.maps.model.LatLng

data class PropertyLatLongEntity(
    val propertyId: Long,
    val latLng: LatLng,
)