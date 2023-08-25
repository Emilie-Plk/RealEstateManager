package com.emplk.realestatemanager.domain.location

import com.emplk.realestatemanager.domain.entities.LocationEntity
import javax.inject.Inject

class AddLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
) {
    suspend fun invoke(location: LocationEntity) {
        locationRepository.addLocation(location)
    }
}
