package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.domain.filter.PropertyMinMaxStatsEntity
import com.emplk.realestatemanager.domain.filter.model.SearchEntity
import kotlinx.coroutines.flow.Flow

interface PropertyRepository {
    suspend fun add(propertyEntity: PropertyEntity): Long?
    suspend fun addPropertyWithDetails(propertyEntity: PropertyEntity): Boolean
    fun getPropertiesAsFlow(): Flow<List<PropertyEntity>>
    fun getPropertiesCountAsFlow(): Flow<Int>
    fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertyEntity?>
    suspend fun getPropertyById(propertyId: Long): PropertyEntity
    suspend fun getMinMaxPricesAndSurfaces(): PropertyMinMaxStatsEntity
    fun getFilteredPropertiesCountRawQuery(searchEntity: SearchEntity): Flow<Int>
    suspend fun update(propertyEntity: PropertyEntity): Boolean
}
