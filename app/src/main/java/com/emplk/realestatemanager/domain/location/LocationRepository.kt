package com.emplk.realestatemanager.domain.location

interface LocationRepository {
    suspend fun add(locationEntity: LocationEntity)

    suspend fun update(locationEntity: LocationEntity)
}