package com.emplk.realestatemanager.domain.connectivity

import kotlinx.coroutines.flow.Flow

interface InternetConnectivityRepository {
    fun isInternetEnabledAsFlow(): Flow<Boolean>
}
