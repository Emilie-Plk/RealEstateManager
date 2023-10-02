package com.emplk.realestatemanager.ui.map

import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.google.android.gms.maps.model.LatLng

data class MarkerViewState(
    val propertyId: Long,
    val latLng: LatLng,
    val onMarkerClicked: EquatableCallbackWithParam<Long>,
)
