package com.emplk.realestatemanager.domain.property_form.picture_preview

import kotlinx.coroutines.flow.Flow

interface PicturePreviewRepository {
    suspend fun add(picturePreviewEntity: PicturePreviewEntity, propertyFormId: Long): Long?

    suspend fun addAll(picturePreviewEntities: List<PicturePreviewEntity>, propertyFormId: Long): List<Long?>

    fun getAllAsFlow(): Flow<List<PicturePreviewEntity>>

    suspend fun getPictureById(picturePreviewId: Long): PicturePreviewEntity?

    suspend fun update(picturePreviewId: Long, isFeatured: Boolean?, description: String?): Boolean

    suspend fun delete(picturePreviewId: Long): Boolean
}
