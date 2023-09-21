package com.emplk.realestatemanager.data.property_form.amenity

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AmenityFormDao {
    @Insert
    suspend fun insert(amenityFormDto: AmenityFormDto): Long?

    @Query("SELECT * FROM amenity_forms")
    fun getAllAsFlow(): Flow<List<AmenityFormDto>>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(amenityFormDto: AmenityFormDto): Int

    @Query("DELETE FROM amenity_forms WHERE id = :amenityFormId")
    suspend fun delete(amenityFormId: Long): Int

    @Query("DELETE FROM amenity_forms WHERE property_form_id = :propertyFormId")
    suspend fun deleteAll(propertyFormId: Long)
}