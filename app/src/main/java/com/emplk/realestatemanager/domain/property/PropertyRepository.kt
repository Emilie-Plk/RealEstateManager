package com.emplk.realestatemanager.domain.property

import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    suspend fun add(propertyEntity: PropertyEntity): Long
    fun getPropertiesAsFlow(): Flow<List<PropertyWithDetailsEntity>>
    fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertyWithDetailsEntity>
    suspend fun update(propertyEntity: PropertyEntity): Int
}
