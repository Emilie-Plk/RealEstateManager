package com.emplk.realestatemanager.data.connectivity

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.core.content.getSystemService
import com.emplk.realestatemanager.domain.connectivity.InternetConnectivityRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject
import javax.inject.Singleton

@Singleton  // thread safe
class InternetConnectivityRepositoryBroadcastReceiver @Inject constructor(
    private val application: Application,
) : InternetConnectivityRepository {

    private val connectivityManager = application.getSystemService<ConnectivityManager>()

    override fun isInternetEnabledAsFlow(): Flow<Boolean> = callbackFlow { // callbackFlow is a cold flow

        @Suppress("DEPRECATION") val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                trySend(
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (connectivityManager?.activeNetwork == null) {
                            false
                        } else {
                            val networkCapabilities =
                                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                            if (networkCapabilities != null) {
                                networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
                            } else {
                                false
                            }
                        }
                    } else {
                        @Suppress("DEPRECATION")
                        connectivityManager?.activeNetworkInfo?.isConnected == true
                    }
                )
            }
        }

        application.registerReceiver(receiver, intentFilter)

        awaitClose {
            application.unregisterReceiver(receiver)
        }
    }.distinctUntilChanged()
}