package com.emplk.realestatemanager.data.property.amenity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface AmenityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(amenityDto: AmenityDto): Long?

    @Query("SELECT * FROM amenities WHERE property_id = :propertyId")
    fun getAllAmenitiesAsFlow(propertyId: Long): Flow<AmenityDto>

    @Update
    suspend fun update(amenityDto: AmenityDto): Int

    @Upsert
    suspend fun upsert(amenityDto: AmenityDto): Long
}