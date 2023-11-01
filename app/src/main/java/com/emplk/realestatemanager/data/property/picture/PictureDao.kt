package com.emplk.realestatemanager.data.property.picture

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
    suspend fun getAllPicturesIds(propertyId: Long): List<Long>

    @Upsert
    suspend fun upsert(pictureDto: PictureDto): Long

    @Query("DELETE FROM pictures WHERE id = :pictureId")
    suspend fun delete(pictureId: Long)
}