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

    @Query("SELECT COUNT(*) FROM properties")
    fun getPropertiesCountAsFlow(): Flow<Int>

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
        "SELECT COUNT(*) FROM properties WHERE " +
                "(:propertyType IS NULL OR type = :propertyType) AND " +
                "((:minPrice = 0 AND :maxPrice = 0) OR " +
                "(:minPrice > 0 AND :maxPrice = 0 AND price >= :minPrice) OR " +
                "(:minPrice = 0 AND :maxPrice > 0 AND price <= :maxPrice) OR " +
                "(:minPrice > 0 AND :maxPrice > 0 AND price BETWEEN :minPrice AND :maxPrice)) AND " +
                "((:minSurface = 0 AND :maxSurface = 0) OR " +  // No filter on price
                "(:minSurface > 0 AND :maxSurface = 0 AND surface >= :minSurface) OR " +
                "(:minSurface = 0 AND :maxSurface > 0 AND surface <= :maxSurface) OR " +
                "(:minSurface > 0 AND :maxSurface > 0 AND surface BETWEEN :minSurface AND :maxSurface)) AND " +  // Both
                "(:amenitySchool IS NULL OR :amenitySchool = 0 OR amenity_school = 1) AND " +
                "(:amenityPark IS NULL OR :amenityPark = 0 OR amenity_park = 1) AND " +
                "(:amenityShopping IS NULL OR :amenityShopping = 0 OR amenity_shopping = 1) AND " +
                "(:amenityRestaurant IS NULL OR :amenityRestaurant = 0 OR amenity_restaurant = 1) AND " +
                "(:amenityConcierge IS NULL OR :amenityConcierge = 0 OR amenity_concierge = 1) AND " +
                "(:amenityGym IS NULL OR :amenityGym = 0 OR amenity_gym = 1) AND " +
                "(:amenityTransportation IS NULL OR :amenityTransportation = 0 OR amenity_transportation = 1) AND " +
                "(:amenityHospital IS NULL OR :amenityHospital = 0 OR amenity_hospital = 1) AND " +
                "(:amenityLibrary IS NULL OR :amenityLibrary = 0 OR amenity_library = 1) AND " +
                "((:entryDateMin IS NULL OR entry_date_epoch >= :entryDateMin) AND (:entryDateMax IS NULL OR entry_date_epoch <= :entryDateMax)) AND " +
                "(:isSold IS NULL OR is_sold = :isSold)"
    )
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
        amenityTransportation: Boolean?,
        amenityHospital: Boolean?,
        amenityLibrary: Boolean?,
        entryDateMin: Long?,
        entryDateMax: Long?,
        isSold: Boolean?
    ): Flow<Int>

    @Update
    suspend fun update(propertyDto: PropertyDto): Int
}
