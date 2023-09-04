package com.emplk.realestatemanager.domain.location

import javax.inject.Inject

class AddLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
) {
    suspend fun invoke(location: LocationEntity) {
        locationRepository.add(location)
    }
}
