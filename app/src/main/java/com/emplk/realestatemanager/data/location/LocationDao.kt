package com.emplk.realestatemanager.data.location

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.emplk.realestatemanager.domain.entities.LocationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert
    suspend fun insert(locationEntity: LocationEntity)

    @Query("SELECT * FROM locations WHERE property_id = :propertyId")
    fun getLocationAsFlow(propertyId: Long): Flow<LocationEntity>

    @Update
    suspend fun update(locationEntity: LocationEntity): Int
}