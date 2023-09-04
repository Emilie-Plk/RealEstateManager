package com.emplk.realestatemanager.data.location

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert
    suspend fun insert(locationDtoEntity: LocationDtoEntity)

    @Query("SELECT * FROM locations WHERE property_id = :propertyId")
    fun getLocationAsFlow(propertyId: Long): Flow<LocationDtoEntity>

    @Update
    suspend fun update(locationDtoEntity: LocationDtoEntity): Int
}