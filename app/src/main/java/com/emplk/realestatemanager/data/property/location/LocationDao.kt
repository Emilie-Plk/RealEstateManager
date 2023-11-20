package com.emplk.realestatemanager.data.property.location

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RewriteQueriesToDropUnusedColumns
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationDto: LocationDto): Long?

    @RewriteQueriesToDropUnusedColumns
    @Query("SELECT * FROM locations")
    fun getAllPropertyLatLongAsFlow(): Flow<List<PropertyLatLongDto>>

    @Query("SELECT * FROM locations")
    fun getAllLocationsWithCursor(): Cursor

    @Update
    suspend fun update(locationDto: LocationDto): Int
}