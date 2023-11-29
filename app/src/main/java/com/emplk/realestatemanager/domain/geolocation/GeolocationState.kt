package com.emplk.realestatemanager.domain.geolocation

import com.emplk.realestatemanager.ui.utils.NativeText

sealed class GeolocationState {
    data class Success(val latitude: Double, val longitude: Double) : GeolocationState()
    data class Error(val message: NativeText?) : GeolocationState()
}
