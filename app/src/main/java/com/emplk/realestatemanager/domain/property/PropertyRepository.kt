package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.domain.filter.PropertyMinMaxStatsEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface PropertyRepository {
    suspend fun add(propertyEntity: PropertyEntity): Long?
    suspend fun addPropertyWithDetails(propertyEntity: PropertyEntity): Boolean
    fun getPropertiesAsFlow(): Flow<List<PropertyEntity>>
    fun getPropertiesCountAsFlow(): Flow<Int>
    fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertyEntity?>
    suspend fun getPropertyById(propertyId: Long): PropertyEntity
    suspend fun getMinMaxPricesAndSurfaces(): PropertyMinMaxStatsEntity
    fun getFilteredPropertiesCount(
        propertyType: String?,
        minPrice: BigDecimal,
        maxPrice: BigDecimal,
        minSurface: BigDecimal,
        maxSurface: BigDecimal,
        amenitySchool: Boolean?,
        amenityPark: Boolean?,
        amenityShopping: Boolean?,
        amenityRestaurant: Boolean?,
        amenityConcierge: Boolean?,
        amenityGym: Boolean?,
        amenityTransport: Boolean?,
        amenityHospital: Boolean?,
        amenityLibrary: Boolean?,
        entryDateEpochMin: Long?,
        entryDateEpochMax: Long?,
        isSold: Boolean?
    ): Flow<Int>

    suspend fun update(propertyEntity: PropertyEntity): Boolean
}
