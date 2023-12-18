package com.emplk.realestatemanager.data.connectivity

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import androidx.annotation.RequiresApi
import com.emplk.realestatemanager.data.DataModule
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
    private val connectivityManager: ConnectivityManager?, // we have to set it to null because... weird Android stuff (Android without internet?..)
    @DataModule.CurrentVersionCode private val currentVersion: Int,
) : InternetConnectivityRepository {

    override fun isInternetEnabledAsFlow(): Flow<Boolean> = callbackFlow {
        trySend(hasInternetConnection())

        @Suppress("DEPRECATION") val intentFilter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                trySend(hasInternetConnection())
            }
        }

        application.registerReceiver(receiver, intentFilter)

        awaitClose {
            application.unregisterReceiver(receiver)
        }
    }.distinctUntilChanged()

    private fun hasInternetConnection(): Boolean {
        if (connectivityManager == null) {
            return false
        }

        @RequiresApi(Build.VERSION_CODES.M)
        if (currentVersion >= Build.VERSION_CODES.M) {
            val activeNetwork: Network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities: NetworkCapabilities? = connectivityManager.getNetworkCapabilities(activeNetwork)
            return networkCapabilities != null &&
                    (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
        } else {
            @Suppress("DEPRECATION")
            return connectivityManager.activeNetworkInfo?.isConnected == true
        }
    }
}