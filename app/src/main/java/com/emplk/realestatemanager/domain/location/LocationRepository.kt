package com.emplk.realestatemanager.domain.location

interface LocationRepository {
    suspend fun add(location: LocationEntity)

    suspend fun update(locationEntity: LocationEntity)
}