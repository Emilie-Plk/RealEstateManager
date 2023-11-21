package com.emplk.realestatemanager.domain.geolocation

import com.emplk.realestatemanager.ui.utils.NativeText
import com.google.android.gms.maps.model.LatLng

sealed class GeolocationState {
    data class Success(val latitude: Double, val longitude: Double) : GeolocationState()
  //  data class LastKnownLocation(val latitude: Double, val longitude: Double) : GeolocationState()
    data class Error(val message: NativeText?) : GeolocationState()
}
