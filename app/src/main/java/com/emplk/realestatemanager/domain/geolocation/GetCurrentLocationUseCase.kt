package com.emplk.realestatemanager.domain.geolocation

import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.connectivity.GpsConnectivityRepository
import com.emplk.realestatemanager.domain.connectivity.IsInternetEnabledFlowUseCase
import com.emplk.realestatemanager.ui.utils.NativeText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val geolocationRepository: GeolocationRepository,
    private val gpsConnectivityRepository: GpsConnectivityRepository,
    private val isInternetEnabledFlowUseCase: IsInternetEnabledFlowUseCase,
) {
    fun invoke(isPermissionGrantedFlow: Flow<Boolean?>): Flow<GeolocationState> =
        combine(
            isPermissionGrantedFlow,
            isInternetEnabledFlowUseCase.invoke(),
            gpsConnectivityRepository.isGpsEnabledAsFlow(),
        ) { isPermissionGranted, isInternetEnabled, isGpsEnabled ->
            if (isPermissionGranted == true && isInternetEnabled && isGpsEnabled) {
                geolocationRepository.getCurrentLocationAsFlow()
            } else if (!isInternetEnabled && !isGpsEnabled) {
                /*  geolocationRepository.getCurrentLocationAsFlow().flatMapLatest { geolocationState ->
                      if (geolocationState is GeolocationState.LastKnownLocation) {
                          flowOf(geolocationState)
                      } else {*/
                flowOf(
                    GeolocationState.Error(
                        NativeText.Resource(R.string.geolocation_error_no_internet),
                    )
                )
                /*           }
                       }*/
            } else if (!isGpsEnabled) {
                flowOf(
                    GeolocationState.Error(
                        NativeText.Resource(R.string.geolocation_error_no_gps),
                    )
                )
            } else {
                flowOf(GeolocationState.Error(null))
            }
        }.flatMapLatest { it }
}
