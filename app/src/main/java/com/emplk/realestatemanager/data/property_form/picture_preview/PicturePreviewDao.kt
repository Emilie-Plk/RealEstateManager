package com.emplk.realestatemanager.data.property_form.picture_preview

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PicturePreviewDao {

    @Insert
    suspend fun insert(picturePreviewDto: PicturePreviewDto): Long

    @Query("SELECT * FROM picture_previews")
    fun getAllAsFlow(): Flow<List<PicturePreviewDto>>

    @Update
    suspend fun update(picturePreviewDto: PicturePreviewDto): Int

    @Upsert
    suspend fun upsert(picturePreviewDto: PicturePreviewDto): Long

    @Query("DELETE FROM picture_previews WHERE id = :picturePreviewId")
    suspend fun delete(picturePreviewId: Long): Int
}