package com.emplk.realestatemanager.data.property.amenity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AmenityDao {

    @Insert
    suspend fun insert(amenityDto: AmenityDto): Long?

    @Query("SELECT * FROM amenities WHERE property_id = :propertyId")
    fun getAllAmenitiesAsFlow(propertyId: Long): Flow<AmenityDto>

    @Update
    suspend fun update(amenityDto: AmenityDto): Int
}