package com.emplk.realestatemanager.domain.geolocation

import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.connectivity.GpsConnectivityRepository
import com.emplk.realestatemanager.domain.connectivity.IsInternetEnabledFlowUseCase
import com.emplk.realestatemanager.domain.permission.HasLocationPermissionFlowUseCase
import com.emplk.realestatemanager.ui.utils.NativeText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val hasLocationPermissionFlowUseCase: HasLocationPermissionFlowUseCase,
    private val isInternetEnabledFlowUseCase: IsInternetEnabledFlowUseCase,
    private val gpsConnectivityRepository: GpsConnectivityRepository,
    private val geolocationRepository: GeolocationRepository,
) {
    fun invoke(): Flow<GeolocationState> =
        combine(
            hasLocationPermissionFlowUseCase.invoke(),
            isInternetEnabledFlowUseCase.invoke(),
            gpsConnectivityRepository.isGpsEnabledAsFlow(),
        ) { isPermissionGranted, isInternetEnabled, isGpsEnabled ->
            if (isPermissionGranted != true) {
                FetchLocationState.NO_PERMISSION
            } else if (!isInternetEnabled) {
                FetchLocationState.NO_INTERNET
            } else if (!isGpsEnabled) {
                FetchLocationState.GPS_DISABLED
            } else {
                FetchLocationState.CAN_FETCH_LOCATION
            }
        }.distinctUntilChanged()
            .flatMapLatest { state ->
                when (state) {
                    FetchLocationState.CAN_FETCH_LOCATION -> geolocationRepository.getCurrentLocationAsFlow()
                    FetchLocationState.GPS_DISABLED -> flowOf(GeolocationState.Error(NativeText.Resource(R.string.geolocation_error_no_gps)))
                    FetchLocationState.NO_INTERNET -> flowOf(GeolocationState.Error(NativeText.Resource(R.string.geolocation_error_no_internet)))
                    FetchLocationState.NO_PERMISSION -> flowOf(GeolocationState.Error(null))
                }
            }
}

private enum class FetchLocationState {
    CAN_FETCH_LOCATION,
    GPS_DISABLED,
    NO_INTERNET,
    NO_PERMISSION,
}
