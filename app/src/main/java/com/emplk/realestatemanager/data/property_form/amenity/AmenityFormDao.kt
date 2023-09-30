package com.emplk.realestatemanager.data.property_form.amenity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AmenityFormDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(amenityFormDto: AmenityFormDto): Long?

    @Insert
    suspend fun insertAll(amenityFormDtos: List<AmenityFormDto>): List<Long?>

    @Query("SELECT * FROM amenity_forms")
    fun getAllAsFlow(): Flow<List<AmenityFormDto>>

    @Query("SELECT id FROM amenity_forms WHERE property_form_id = :propertyFormId")
    suspend fun getAllIds(propertyFormId: Long): List<Long>

    @Query("DELETE FROM amenity_forms WHERE id = :amenityFormId")
    suspend fun delete(amenityFormId: Long): Int

    @Query("DELETE FROM amenity_forms WHERE property_form_id = :propertyFormId")
    suspend fun deleteAll(propertyFormId: Long): Int?
}