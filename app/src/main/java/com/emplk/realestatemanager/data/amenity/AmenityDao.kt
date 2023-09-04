package com.emplk.realestatemanager.data.amenity

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

interface AmenityDao {

    @Insert
    suspend fun insertAmenity(amenity: AmenityDtoEntity)

    @Query("SELECT * FROM amenities WHERE property_id = :propertyId")
    fun getAllAmenitiesAsFlow(propertyId: Long): Flow<List<AmenityDtoEntity>>

    @Update
    suspend fun updateAmenity(amenity: AmenityDtoEntity)
}