package com.emplk.realestatemanager.domain.map

import com.emplk.realestatemanager.domain.property.location.LocationRepository
import com.emplk.realestatemanager.domain.property.location.PropertyLatLongEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllPropertiesLatLongUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    fun invoke(): Flow<List<PropertyLatLongEntity>> = locationRepository.getAllPropertyLatLongAsFlow()
}