package com.emplk.realestatemanager.data.property.location

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Update

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationDto: LocationDto): Long?

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM locations")
    suspend fun getAllPropertyLatLong(): List<PropertyLatLongDto>

    @Update
    suspend fun update(locationDto: LocationDto): Int
}