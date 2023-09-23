package com.emplk.realestatemanager.data.property_form.picture_preview

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PicturePreviewDao {

    @Insert
    suspend fun insert(picturePreviewFormDto: PicturePreviewFormDto): Long?

    @Query("SELECT * FROM picture_previews")
    fun getAllAsFlow(): Flow<List<PicturePreviewFormDto>>

    @Query("UPDATE picture_previews SET uri = :newUri, description = :newDescription, isFeatured = :newIsFeatured WHERE property_form_id = :propertyFormId")
    suspend fun update(newUri: String?, newDescription: String?, newIsFeatured: Boolean?, propertyFormId: Long): Int

    @Query("DELETE FROM picture_previews WHERE id = :picturePreviewId")
    suspend fun delete(picturePreviewId: Long): Int

    @Query("DELETE FROM picture_previews WHERE property_form_id = :propertyFormId")
    suspend fun deleteAll(propertyFormId: Long)
}