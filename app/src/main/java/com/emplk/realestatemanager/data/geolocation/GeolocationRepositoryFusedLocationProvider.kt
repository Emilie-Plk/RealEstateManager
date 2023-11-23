package com.emplk.realestatemanager.data.geolocation

import android.util.Log
import androidx.annotation.RequiresPermission
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.geolocation.GeolocationRepository
import com.emplk.realestatemanager.domain.geolocation.GeolocationState
import com.emplk.realestatemanager.ui.utils.NativeText
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.seconds

@Singleton
class GeolocationRepositoryFusedLocationProvider @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : GeolocationRepository {

    companion object {
        private val LOCATION_INTERVAL_DURATION = 5.seconds
        private val LOCATION_MAX_UPDATE_DELAY = 10.seconds
        private const val LOCATION_DISTANCE_THRESHOLD = 50F
    }

    @RequiresPermission(anyOf = ["android.permission.ACCESS_COARSE_LOCATION", "android.permission.ACCESS_FINE_LOCATION"])
    override fun getCurrentLocationAsFlow(): Flow<GeolocationState> = callbackFlow {
        /*   // trysend of the last known location if available and if not too old
            fusedLocationProviderClient.lastLocation?.let {
                   if (it.time > System.currentTimeMillis() - LOCATION_MAX_UPDATE_DELAY.inWholeMilliseconds) {
                    trySend(GeolocationState.Success(it.latitude, it.longitude))
                   }
             }*/
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    trySend(
                        GeolocationState.Success(
                            it.latitude,
                            it.longitude
                        )
                    )
                    Log.d("COUCOU", "Location sent: ${it.latitude}, ${it.longitude}")
                } ?: trySend(GeolocationState.Error(NativeText.Resource(R.string.geolocation_error_no_location_found)))
            }
        }

        fusedLocationProviderClient.requestLocationUpdates(
            LocationRequest.Builder(LOCATION_INTERVAL_DURATION.inWholeMilliseconds)
                .setMinUpdateDistanceMeters(LOCATION_DISTANCE_THRESHOLD)
                .setMaxUpdateDelayMillis(LOCATION_MAX_UPDATE_DELAY.inWholeMilliseconds)
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .build(),
            coroutineDispatcherProvider.io.asExecutor(),
            locationCallback
        )

        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }.flowOn(coroutineDispatcherProvider.io)
}