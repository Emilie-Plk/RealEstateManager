package com.emplk.realestatemanager.data.property

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.emplk.realestatemanager.domain.filter.PropertyMinMaxStatsEntity
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDateTime

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

    // I want to select price if it is in the range of minPrice and maxPrice
    // if minPrice is 0, it means that the user didn't set any value
    @Query(
        "SELECT COUNT(*) FROM properties WHERE " +
                "(:propertyType IS NULL OR type = :propertyType) AND " +
                "(price BETWEEN (SELECT MIN(price) FROM properties) AND (SELECT MAX(price) FROM properties) OR (:minPrice = 0 AND :maxPrice = 0)) AND " +
                "(surface BETWEEN (SELECT MIN(surface) FROM properties) AND (SELECT MAX(surface) FROM properties) OR (:minSurface = 0 AND :maxSurface = 0)) AND " +
                "(:amenitySchool IS NULL OR :amenitySchool = 0 OR amenity_school = :amenitySchool) AND " +
                "(:amenityPark IS NULL OR :amenityPark = 0 OR amenity_park = :amenityPark) AND " +
                "(:amenityShopping IS NULL OR :amenityShopping = 0 OR amenity_shopping = :amenityShopping) AND " +
                "(:amenityRestaurant IS NULL OR :amenityRestaurant = 0 OR amenity_restaurant = :amenityRestaurant) AND " +
                "(:amenityConcierge IS NULL OR :amenityConcierge = 0 OR amenity_concierge = :amenityConcierge) AND " +
                "(:amenityGym IS NULL OR :amenityGym = 0 OR amenity_gym = :amenityGym) AND " +
                "(:amenityTransportation IS NULL OR :amenityTransportation = 0 OR amenity_transportation = :amenityTransportation) AND " +
                "(:amenityHospital IS NULL OR :amenityHospital = 0 OR amenity_hospital = :amenityHospital) AND " +
                "(:amenityLibrary IS NULL OR :amenityLibrary = 0 OR amenity_library = :amenityLibrary) AND " +
                "(:entryDateMin IS NULL OR entry_date >= :entryDateMin) AND " +
                "(:entryDateMax IS NULL OR entry_date <= :entryDateMax) AND " +
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
        entryDateMin: LocalDateTime?,
        entryDateMax: LocalDateTime?,
        isSold: Boolean?
    ): Flow<Int>

    @Update
    suspend fun update(propertyDto: PropertyDto): Int
}
