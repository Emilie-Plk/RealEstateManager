package com.emplk.realestatemanager.domain.geolocation

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val geolocationRepository: GeolocationRepository,
) {
    fun invoke(): Flow<GeolocationEntity?> = geolocationRepository
        .getCurrentLocationAsFlow()
}
