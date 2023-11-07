package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.data.property.PropertyIdWithLatLong
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface PropertyRepository {
    suspend fun add(propertyEntity: PropertyEntity): Long?
    suspend fun addPropertyWithDetails(propertyEntity: PropertyEntity): Boolean
    fun getPropertiesAsFlow(): Flow<List<PropertyEntity>>
    fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertyEntity?>
    suspend fun getPropertyById(propertyId: Long): PropertyEntity
    suspend fun getFilteredProperties(
        propertyType: String?,
        minPrice: BigDecimal?,
        maxPrice: BigDecimal?,
        minSurface: BigDecimal?,
        maxSurface: BigDecimal?,
        entryDate: String?,
        isSold: Boolean?
    ): List<PropertyIdWithLatLong>

    suspend fun update(propertyEntity: PropertyEntity): Boolean
}
