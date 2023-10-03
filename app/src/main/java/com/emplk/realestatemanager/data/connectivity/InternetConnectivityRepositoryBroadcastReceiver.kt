package com.emplk.realestatemanager.data.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager
import android.net.ConnectivityManager
import android.util.Log
import com.emplk.realestatemanager.domain.connectivity.InternetConnectivityRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class InternetConnectivityRepositoryBroadcastReceiver @Inject constructor(
    @ApplicationContext private val context: Context,
) : InternetConnectivityRepository,
    BroadcastReceiver() {
    private val isInternetEnabledMutableStateFlow: MutableStateFlow<Boolean?> = MutableStateFlow(null)

    override fun isInternetEnabledAsFlow(): Flow<Boolean?> = isInternetEnabledMutableStateFlow.asStateFlow()

    @Suppress("DEPRECATION")
    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent?.action
        if (action != null && action == ConnectivityManager.CONNECTIVITY_ACTION) {
            val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            val isConnected = networkInfo != null && networkInfo.isConnected
            isInternetEnabledMutableStateFlow.tryEmit(isConnected)
            Log.d("COUCOU", "onReceive: Internet is connected: $isConnected")
        }
    }
}