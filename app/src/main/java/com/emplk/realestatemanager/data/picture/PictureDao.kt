package com.emplk.realestatemanager.data.picture

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PictureDao {

    @Insert
    suspend fun insert(pictureDto: PictureDto): Long

    @Query("SELECT * FROM pictures WHERE property_id = :propertyId")
    fun getPicturesAsFlow(propertyId: Long): Flow<List<PictureDto>>

    @Update
    suspend fun update(pictureDto: PictureDto): Int

    @Query("DELETE FROM pictures WHERE id = :pictureId")
    suspend fun delete(pictureId: Long): Int
}