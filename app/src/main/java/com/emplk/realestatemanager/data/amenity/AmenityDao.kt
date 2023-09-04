package com.emplk.realestatemanager.data.amenity

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.emplk.realestatemanager.domain.amenity.AmenityEntity
import kotlinx.coroutines.flow.Flow

interface AmenityDao {

    @Insert
    suspend fun insertAmenity(amenity: AmenityEntity)

    @Query("SELECT * FROM amenities WHERE property_id = :propertyId")
    fun getAllAmenitiesAsFlow(propertyId: Long): Flow<List<AmenityEntity>>

    @Update
    suspend fun updateAmenity(amenity: AmenityEntity)
}