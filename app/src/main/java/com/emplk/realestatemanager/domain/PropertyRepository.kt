package com.emplk.realestatemanager.domain

import com.emplk.realestatemanager.domain.add_property.entities.PropertyEntity
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    suspend fun add(propertyEntity: PropertyEntity)
    suspend fun update(propertyEntity: PropertyEntity): Int
    fun getPropertiesAsFlow(): Flow<List<PropertyEntity>>
    fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertyEntity>
}
