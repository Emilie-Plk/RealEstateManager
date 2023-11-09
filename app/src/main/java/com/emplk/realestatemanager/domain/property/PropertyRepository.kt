package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.domain.filter.PropertyMinMaxStatsEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDateTime

interface PropertyRepository {
    suspend fun add(propertyEntity: PropertyEntity): Long?
    suspend fun addPropertyWithDetails(propertyEntity: PropertyEntity): Boolean
    fun getPropertiesAsFlow(): Flow<List<PropertyEntity>>
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
        entryDateMin: LocalDateTime?,
        entryDateMax: LocalDateTime?,
        isSold: Boolean?
    ): Flow<Int>

    suspend fun update(propertyEntity: PropertyEntity): Boolean
}
