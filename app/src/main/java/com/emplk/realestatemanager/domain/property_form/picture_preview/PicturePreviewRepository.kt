package com.emplk.realestatemanager.domain.property_form.picture_preview

import kotlinx.coroutines.flow.Flow

interface PicturePreviewRepository {
    suspend fun add(picturePreviewEntity: PicturePreviewEntity): Long

    fun getAsFlow(): Flow<List<PicturePreviewEntity>>

    suspend fun update(picturePreviewEntity: PicturePreviewEntity): Boolean

    suspend fun delete(picturePreviewId: Long): Boolean
}
