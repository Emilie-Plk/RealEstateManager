package com.emplk.realestatemanager.data.property_form.amenity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AmenityFormDao {
    @Insert
    suspend fun insert(amenityFormDto: AmenityFormDto): Long

    @Insert
    fun getAllAsFlow(): Flow<List<AmenityFormDto>>

    @Query("DELETE FROM amenity_forms WHERE id = :amenityFormId")
    suspend fun delete(amenityFormId: Long): Int
}