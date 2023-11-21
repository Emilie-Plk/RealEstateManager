package com.emplk.realestatemanager.ui.map

import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.google.android.gms.maps.model.LatLng

data class MarkerViewState(
    val userCurrentLocation: LatLng?,
    val fallbackLocationGoogleHq: LatLng,
    val propertyMarkers: List<PropertyMarkerViewState>,
)

data class PropertyMarkerViewState(
    val propertyId: Long,
    val latLng: LatLng,
    val onMarkerClicked: EquatableCallbackWithParam<Long>,
)
