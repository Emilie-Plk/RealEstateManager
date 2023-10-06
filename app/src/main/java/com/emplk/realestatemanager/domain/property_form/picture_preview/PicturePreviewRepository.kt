package com.emplk.realestatemanager.domain.property_form.picture_preview

import android.net.Uri
import kotlinx.coroutines.flow.Flow

interface PicturePreviewRepository {
    suspend fun add(picturePreviewEntity: PicturePreviewEntity, propertyFormId: Long): Long?

    suspend fun addAll(picturePreviewEntities: List<PicturePreviewEntity>, propertyFormId: Long): List<Long?>

    fun getAllAsFlow(propertyFormId: Long): Flow<List<PicturePreviewEntity>>

    suspend fun getAll(propertyFormId: Long): List<PicturePreviewEntity>

    suspend fun getPictureById(picturePreviewId: Long): PicturePreviewEntity?

    suspend fun update(picturePreviewId: Long, isFeatured: Boolean?, description: String?): Boolean

    suspend fun delete(picturePreviewId: Long): Boolean

    suspend fun deleteAll(picturePreviewId: Long): Boolean
}
