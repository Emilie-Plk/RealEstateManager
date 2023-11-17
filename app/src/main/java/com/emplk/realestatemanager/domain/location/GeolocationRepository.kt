package com.emplk.realestatemanager.domain.location

import kotlinx.coroutines.flow.Flow

interface GeolocationRepository {
fun getCurrentLocationAsFlow(): Flow<GeolocationEntity>
}
