package com.emplk.realestatemanager.data.property_form.location

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationFormDao {

    @Insert
    suspend fun insert(locationFormDto: LocationFormDto): Long?

    @Query("SELECT * FROM location_forms WHERE property_form_id = :propertyFormId LIMIT 1")
    fun getAsFlow(propertyFormId: Long): Flow<LocationFormDto>

    @Query("UPDATE location_forms SET address = :newAddress, " +
            "latitude = :newLatitude, " +
            "longitude = :newLongitude WHERE property_form_id = :propertyFormId")
    fun update(
        newAddress: String?,
        newLatitude: String?,
        newLongitude: String?,
        propertyFormId: Long
    ): Int

    @Query("DELETE FROM location_forms WHERE property_form_id = :propertyFormId")
    suspend fun delete(propertyFormId: Long)
}