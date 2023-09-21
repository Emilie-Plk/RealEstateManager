package com.emplk.realestatemanager.data.property_form.location

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationFormDao {

    @Insert
    suspend fun insert(locationFormDto: LocationFormDto): Long?

    @Query("SELECT * FROM location_forms WHERE property_form_id = :propertyFormId LIMIT 1")
    fun getAsFlow(propertyFormId: Long): Flow<LocationFormDto>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(locationFormDto: LocationFormDto): Int

    @Query("DELETE FROM location_forms WHERE property_form_id = :propertyFormId")
    suspend fun delete(propertyFormId: Long)
}