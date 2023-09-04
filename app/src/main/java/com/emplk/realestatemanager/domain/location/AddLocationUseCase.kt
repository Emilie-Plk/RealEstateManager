package com.emplk.realestatemanager.domain.location

import com.emplk.realestatemanager.data.location.LocationDtoEntity
import javax.inject.Inject

class AddLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository,
) {
    suspend fun invoke(location: LocationDtoEntity) {
        locationRepository.add(location)
    }
}
