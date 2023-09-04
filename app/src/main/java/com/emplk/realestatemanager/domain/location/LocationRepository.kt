package com.emplk.realestatemanager.domain.location

import com.emplk.realestatemanager.data.location.LocationDtoEntity

interface LocationRepository {
    suspend fun add(location: LocationDtoEntity)

    suspend fun update(locationDtoEntity: LocationDtoEntity)
}