package com.emplk.realestatemanager.data.property.location

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationDto: LocationDto): Long?

    @Query("SELECT * FROM locations WHERE property_id = :propertyId LIMIT 1")
    fun getLocationAsFlow(propertyId: Long): Flow<LocationDto>

    @Update
    suspend fun update(locationDto: LocationDto): Int
}