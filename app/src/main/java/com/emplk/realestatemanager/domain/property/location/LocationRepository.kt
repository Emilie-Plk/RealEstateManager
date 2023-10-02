package com.emplk.realestatemanager.domain.property.location

import com.emplk.realestatemanager.data.property.location.PropertyLatLongDto

interface LocationRepository {
    suspend fun add(locationEntity: LocationEntity, propertyId: Long): Boolean

    suspend fun getAllPropertyLatLong(): List<PropertyLatLongEntity>

    suspend fun update(locationEntity: LocationEntity, propertyId: Long): Boolean
}