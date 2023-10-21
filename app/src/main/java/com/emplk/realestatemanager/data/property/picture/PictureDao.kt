package com.emplk.realestatemanager.data.property.picture

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(pictureDto: PictureDto): Long?

    @Query("SELECT * FROM pictures WHERE property_id = :propertyId")
    fun getPicturesAsFlow(propertyId: Long): Flow<List<PictureDto>>

    @Update
    suspend fun update(pictureDto: PictureDto): Int

    @Upsert
    suspend fun upsert(pictureDto: PictureDto): Long

    @Query("DELETE FROM pictures WHERE id = :pictureId")
    suspend fun delete(pictureId: Long): Int
}