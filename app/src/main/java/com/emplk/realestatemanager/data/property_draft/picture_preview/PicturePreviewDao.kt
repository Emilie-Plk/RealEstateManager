package com.emplk.realestatemanager.data.property_draft.picture_preview

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface PicturePreviewDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(picturePreviewDto: PicturePreviewDto): Long

    @Insert
    suspend fun insertAll(picturePreviewDtos: List<PicturePreviewDto>): List<Long?>

    @Query("SELECT * FROM picture_previews WHERE property_draft_id = :propertyFormId")
    fun getAllAsFlow(propertyFormId: Long): Flow<List<PicturePreviewDto>>

    @Query("SELECT * FROM picture_previews WHERE property_draft_id = :propertyFormId")
    suspend fun getAll(propertyFormId: Long): List<PicturePreviewDto>

    @Query("SELECT id FROM picture_previews WHERE property_draft_id = :propertyFormId")
    suspend fun getAllIds(propertyFormId: Long): List<Long>

    @Query("SELECT * FROM picture_previews WHERE id = :picturePreviewId LIMIT 1")
    suspend fun getPictureById(picturePreviewId: Long): PicturePreviewDto?

    @Query("UPDATE picture_previews SET is_featured = :newIsFeatured, description = :newDescription WHERE id = :picturePreviewId")
    suspend fun update(picturePreviewId: Long, newIsFeatured: Boolean?, newDescription: String?): Int

    @Query("UPDATE picture_previews SET is_featured = 0 WHERE is_featured = 1")
    suspend fun clearFeaturedPicture(): Int

    @Query("DELETE FROM picture_previews WHERE id = :picturePreviewId")
    suspend fun delete(picturePreviewId: Long): Int

    @Query("DELETE FROM picture_previews WHERE property_draft_id = :propertyFormId")
    suspend fun deleteAll(propertyFormId: Long): Int?
}