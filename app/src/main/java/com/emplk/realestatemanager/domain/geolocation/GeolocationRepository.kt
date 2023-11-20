package com.emplk.realestatemanager.domain.geolocation

import kotlinx.coroutines.flow.Flow

interface GeolocationRepository {
    fun getCurrentLocationAsFlow(): Flow<GeolocationState>
}
