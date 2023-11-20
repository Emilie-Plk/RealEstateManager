package com.emplk.realestatemanager.domain.geolocation

sealed class GeolocationState {
    data class Success(val latitude: Double, val longitude: Double) : GeolocationState()
    object NoLocationAvailable : GeolocationState()
    object NoLocationWithMissingPermission : GeolocationState()
}
