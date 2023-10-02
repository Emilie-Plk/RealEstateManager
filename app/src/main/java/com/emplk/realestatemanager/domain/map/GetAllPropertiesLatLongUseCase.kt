package com.emplk.realestatemanager.domain.map

import com.emplk.realestatemanager.domain.property.location.LocationRepository
import com.emplk.realestatemanager.domain.property.location.PropertyLatLongEntity
import javax.inject.Inject

class GetAllPropertiesLatLongUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend fun invoke(): List<PropertyLatLongEntity> =
        locationRepository.getAllPropertyLatLong()
}