package com.emplk.realestatemanager.data.geolocation

import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.location.GeolocationEntity
import com.emplk.realestatemanager.domain.location.GeolocationRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback

import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

@Singleton
class GeolocationRepositoryFusedLocationProvider @Inject constructor(
    private val fusedLocationProvideClient: FusedLocationProviderClient,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : GeolocationRepository {

    companion object {
        private val LOCATION_INTERVAL_DURATION = 10.seconds
        private val LOCATION_MAX_UPDATE_DELAY = 1.minutes
        private const val LOCATION_DISTANCE_THRESHOLD = 50F
    }

    @SuppressWarnings("MissingPermission")
    override fun getCurrentLocationAsFlow(): Flow<GeolocationEntity> = callbackFlow {
        val locationRequest =
            LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, LOCATION_INTERVAL_DURATION.inWholeMicroseconds)
                .setMinUpdateDistanceMeters(LOCATION_DISTANCE_THRESHOLD)
                .setMaxUpdateDelayMillis(LOCATION_MAX_UPDATE_DELAY.inWholeMilliseconds)
                .build()

        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let {
                    trySend(
                        GeolocationEntity(
                            it.latitude,
                            it.longitude
                        )
                    )
                }
            }
        }

        fusedLocationProvideClient.requestLocationUpdates(
            locationRequest,
            coroutineDispatcherProvider.io.asExecutor(),
            locationCallback
        )

        awaitClose {
            fusedLocationProvideClient.removeLocationUpdates(locationCallback)
        }
    }
}