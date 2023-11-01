package com.emplk.realestatemanager.domain.property.location

interface LocationRepository {
    suspend fun add(locationEntity: LocationEntity, propertyId: Long): Boolean

    suspend fun getAllPropertyLatLong(): List<PropertyLatLongEntity>
}