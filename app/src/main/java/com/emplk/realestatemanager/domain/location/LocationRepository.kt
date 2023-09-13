package com.emplk.realestatemanager.domain.location

interface LocationRepository {
    suspend fun add(locationEntity: LocationEntity, propertyId: Long): Boolean

    suspend fun update(locationEntity: LocationEntity, propertyId: Long): Boolean
}