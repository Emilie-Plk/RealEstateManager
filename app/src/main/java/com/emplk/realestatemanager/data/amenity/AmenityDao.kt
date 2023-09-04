package com.emplk.realestatemanager.data.amenity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AmenityDao {

    @Insert
    suspend fun insert(amenity: AmenityDtoEntity)

    @Query("SELECT * FROM amenities WHERE property_id = :propertyId")
    fun getAllAmenitiesAsFlow(propertyId: Long): Flow<AmenityDtoEntity>

    @Update
    suspend fun update(amenity: AmenityDtoEntity)
}