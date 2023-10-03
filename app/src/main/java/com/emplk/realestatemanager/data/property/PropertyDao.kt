package com.emplk.realestatemanager.data.property

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Transaction
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PropertyDao {

    @Insert
    suspend fun insert(propertyDto: PropertyDto): Long

    @Transaction
    @Query("SELECT * FROM properties WHERE id = :propertyId")
    fun getPropertyByIdAsFlow(propertyId: Long): Flow<PropertyWithDetails>

    @Query("SELECT * FROM properties")
    fun getPropertiesAsFlow(): Flow<List<PropertyDto>>

    @RewriteQueriesToDropUnusedColumns
    @Query(
        "SELECT properties.*, pictures.uri " +
                "FROM properties " +
                "INNER JOIN pictures ON properties.id = pictures.property_id " +
               // "LEFT JOIN amenities ON properties.id = amenities.property_id " +
                "WHERE properties.id = :propertyId LIMIT 1"
    )
    suspend fun getPropertyTypePriceAndSurfaceById(propertyId: Long): PropertyTypeSurfacePriceAndPictureDto

    @Transaction
    @Query("SELECT * FROM properties")
    fun getPropertiesWithDetailsAsFlow(): Flow<List<PropertyWithDetails>>

    @Update
    suspend fun update(propertyDto: PropertyDto): Int
}