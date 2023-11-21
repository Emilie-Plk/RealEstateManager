package com.emplk.realestatemanager.domain.connectivity

import kotlinx.coroutines.flow.Flow

interface GpsConnectivityRepository {
    fun isGpsEnabledAsFlow(): Flow<Boolean>
}
