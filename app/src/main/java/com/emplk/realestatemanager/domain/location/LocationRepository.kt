package com.emplk.realestatemanager.domain.location

interface LocationRepository {
    suspend fun addLocation(location: LocationEntity)

    suspend fun updateLocation(locationEntity: LocationEntity)
}