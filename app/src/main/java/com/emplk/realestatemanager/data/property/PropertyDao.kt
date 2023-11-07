package com.emplk.realestatemanager.data.property

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.emplk.realestatemanager.domain.filter.PropertyMinMaxStatsEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

@Dao
interface PropertyDao {

    @Insert
    suspend fun insert(propertyDto: PropertyDto): Long

    @Transaction
    @Query("SELECT * FROM properties WHERE id = :propertyId")
    fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertyWithDetails?>

    @Transaction
    @Query("SELECT * FROM properties WHERE id = :propertyId")
    suspend fun getPropertyById(propertyId: Long): PropertyWithDetails?

    @Transaction
    @Query("SELECT * FROM properties")
    fun getPropertiesWithDetailsAsFlow(): Flow<List<PropertyWithDetails>>

    @Query(
        "SELECT " +
                "MIN(price) AS minPrice, " +
                "MAX(price) AS maxPrice, " +
                "MIN(surface) AS minSurface, " +
                "MAX(surface) AS maxSurface " +
                "FROM properties"
    )
    suspend fun getMinMaxPricesAndSurfaces(): PropertyMinMaxStatsEntity

    @Query(
        "SELECT properties.id, locations.latitude, locations.longitude FROM properties " +
                "INNER JOIN locations ON properties.id = locations.property_id " +
                "WHERE (:propertyType IS NULL OR type = :propertyType) " +
                "AND (:minPrice IS NULL OR price > :minPrice) " +
                "AND (:maxPrice IS NULL OR price < :maxPrice) " +
                "AND (:minSurface IS NULL OR surface > :minSurface) " +
                "AND (:maxSurface IS NULL OR surface < :maxSurface) " +
                "AND (:entryDate IS NULL OR entry_date = :entryDate) " +
                "AND (:isSold IS NULL OR (:isSold = 1 AND sale_date IS NOT NULL) OR " +
                "(:isSold = 0 AND sale_date IS NULL))"
    )
    suspend fun getFilteredPropertiesIds(
        propertyType: String?,
        minPrice: BigDecimal?,
        maxPrice: BigDecimal?,
        minSurface: BigDecimal?,
        maxSurface: BigDecimal?,
        entryDate: String?,
        isSold: Boolean?
    ): List<PropertyIdWithLatLong>

    @Update
    suspend fun update(propertyDto: PropertyDto): Int
}
