package com.emplk.realestatemanager.data.property_form.picture_preview

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PicturePreviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(picturePreviewFormDto: PicturePreviewFormDto): Long

    @Insert
    suspend fun insertAll(picturePreviewFormDtos: List<PicturePreviewFormDto>): List<Long?>

    @Query("SELECT * FROM picture_previews")
    fun getAllAsFlow(): Flow<List<PicturePreviewFormDto>>

    @Query("SELECT id FROM picture_previews WHERE property_form_id = :propertyFormId")
    suspend fun getAllIds(propertyFormId: Long): List<Long>

    @Query("UPDATE picture_previews SET is_featured = :newIsFeatured, description = :newDescription WHERE id = :picturePreviewId")
    suspend fun update(newIsFeatured: Boolean?, newDescription: String?, picturePreviewId: Long): Int

    @Query("DELETE FROM picture_previews WHERE id = :picturePreviewId")
    suspend fun delete(picturePreviewId: Long): Int

    @Query("DELETE FROM picture_previews WHERE property_form_id = :propertyFormId")
    suspend fun deleteAll(propertyFormId: Long)
}