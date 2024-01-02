package com.emplk.realestatemanager.data.property

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Transaction
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.emplk.realestatemanager.domain.filter.model.PropertyMinMaxStatsEntity
import kotlinx.coroutines.flow.Flow

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

    @RawQuery(observedEntities = [PropertyDto::class])
    fun getFilteredPropertiesCountRawQuery(query: SupportSQLiteQuery): Flow<Int>

    @Query("SELECT * FROM properties")
    fun getAllPropertiesWithCursor(): Cursor

    @Query("SELECT * FROM properties WHERE id = :propertyId")
    fun getPropertyByIdWithCursor(propertyId: Long): Cursor

    @Update
    suspend fun update(propertyDto: PropertyDto): Int
}
