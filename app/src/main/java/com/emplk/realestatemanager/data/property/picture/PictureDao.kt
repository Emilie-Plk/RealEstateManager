package com.emplk.realestatemanager.data.property.picture

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pictureDto: PictureDto): Long?

    @Query("SELECT id FROM pictures WHERE property_id = :propertyId")
    suspend fun getAllPicturesIdsFromProperty(propertyId: Long): List<Long>

    @Query("SELECT * FROM pictures")
    fun getAllPicturesWithCursor(): Cursor

    @Upsert
    suspend fun upsert(pictureDto: PictureDto): Long

    @Query("DELETE FROM pictures WHERE id = :pictureId")
    suspend fun delete(pictureId: Long)
}