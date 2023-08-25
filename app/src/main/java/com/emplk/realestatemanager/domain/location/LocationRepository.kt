package com.emplk.realestatemanager.domain.location

import com.emplk.realestatemanager.domain.entities.LocationEntity

interface LocationRepository {
    suspend fun addLocation(location: LocationEntity)

    suspend fun updateLocation(locationEntity: LocationEntity)
}