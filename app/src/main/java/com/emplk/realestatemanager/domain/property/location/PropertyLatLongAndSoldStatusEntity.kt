package com.emplk.realestatemanager.domain.property.location

import com.google.android.gms.maps.model.LatLng

data class PropertyLatLongAndSoldStatusEntity(
    val propertyId: Long,
    val latLng: LatLng,
    val isSold: Boolean,
)