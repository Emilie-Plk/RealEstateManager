package com.emplk.realestatemanager.domain.property

import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    suspend fun add(propertyEntity: PropertyEntity): Long?
    suspend fun addPropertyWithDetails(propertyEntity: PropertyEntity): Boolean
    fun getPropertiesAsFlow(): Flow<List<PropertyEntity>>
    fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertyEntity>
    suspend fun getPropertyById(propertyId: Long): PropertyEntity
    suspend fun update(propertyEntity: PropertyEntity): Boolean
}
