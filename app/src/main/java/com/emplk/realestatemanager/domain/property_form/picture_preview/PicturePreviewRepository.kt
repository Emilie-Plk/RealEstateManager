package com.emplk.realestatemanager.domain.property_form.picture_preview

import kotlinx.coroutines.flow.Flow

interface PicturePreviewRepository {
    suspend fun add(picturePreviewEntity: PicturePreviewEntity): Long

    fun getAsFlow(): Flow<List<PicturePreviewEntity>>

    suspend fun update(picturePreviewEntity: PicturePreviewEntity): Boolean

    suspend fun upsert(picturePreviewEntity: PicturePreviewEntity): Long

    suspend fun updateDescription(pictureId: Long, newDescription: String?)

    suspend fun updateFeaturedPicture(pictureId: Long, newFeaturedPicture: Boolean)

    suspend fun delete(picturePreviewId: Long): Boolean
}
