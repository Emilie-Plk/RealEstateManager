package com.emplk.realestatemanager.domain.property_draft.picture_preview

import kotlinx.coroutines.flow.Flow

interface PicturePreviewRepository {
    suspend fun add(picturePreviewEntity: PicturePreviewEntity, propertyFormId: Long): Long

    fun getAllAsFlow(propertyFormId: Long): Flow<List<PicturePreviewEntity>>

    suspend fun getAll(propertyFormId: Long): List<PicturePreviewEntity>

    suspend fun update(picturePreviewId: Long, isFeatured: Boolean?, description: String?): Boolean

    suspend fun delete(picturePreviewId: Long): Boolean

    suspend fun deleteAll(picturePreviewId: Long): Boolean
}
