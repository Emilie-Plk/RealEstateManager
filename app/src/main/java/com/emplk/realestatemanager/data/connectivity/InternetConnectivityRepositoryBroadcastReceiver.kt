package com.emplk.realestatemanager.data.connectivity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import com.emplk.realestatemanager.domain.connectivity.InternetConnectivityRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject


class InternetConnectivityRepositoryBroadcastReceiver @Inject constructor() : InternetConnectivityRepository,
    BroadcastReceiver() {
    private val isInternetEnabledMutableStateFlow: MutableStateFlow<Boolean> = MutableStateFlow(false)

    override fun isInternetEnabledAsFlow(): Flow<Boolean> = isInternetEnabledMutableStateFlow

    @Suppress("DEPRECATION")
    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("COUCOU", "onReceive tiramisu 1st: ${isInternetEnabledMutableStateFlow.value}")
        val connectivityManager = context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (connectivityManager.activeNetwork == null) {
                isInternetEnabledMutableStateFlow.tryEmit(false)
                Log.d("COUCOU", "onReceive tiramisu: false")
            } else {
                val networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (networkCapabilities != null) {
                    isInternetEnabledMutableStateFlow.tryEmit(
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                    )
                    Log.d(
                        "COUCOU",
                        "onReceive tiramisu: ${networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)}"
                    )
                } else {
                    isInternetEnabledMutableStateFlow.tryEmit(false)
                    Log.d("COUCOU", "onReceive tiramisu: false")
                }
            }
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo
            isInternetEnabledMutableStateFlow.tryEmit(networkInfo != null && networkInfo.isConnected)
            Log.d("COUCOU", "onReceive tiramisu old version: ${networkInfo != null && networkInfo.isConnected}")
        }
    }
}