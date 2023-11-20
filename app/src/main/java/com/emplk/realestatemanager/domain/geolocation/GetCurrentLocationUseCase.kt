package com.emplk.realestatemanager.domain.geolocation

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val geolocationRepository: GeolocationRepository,
) {
    fun invoke(isPermissionGrantedFlow: Flow<Boolean?>): Flow<GeolocationState> =
        isPermissionGrantedFlow.flatMapLatest { isPermissionGranted ->
            when (isPermissionGranted) {
                true -> geolocationRepository.getCurrentLocationAsFlow()
                false, null -> flowOf(GeolocationState.NoLocationWithMissingPermission)
            }
        }
}
