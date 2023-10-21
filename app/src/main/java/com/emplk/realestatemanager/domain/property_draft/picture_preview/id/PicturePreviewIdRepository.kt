package com.emplk.realestatemanager.domain.property_draft.picture_preview.id

import kotlinx.coroutines.flow.Flow

interface PicturePreviewIdRepository {
    fun add(picturePreviewId: Long)
    fun addAll(picturePreviewIds: List<Long>)
    fun getAllAsFlow(): Flow<List<Long>>
    fun delete(picturePreviewId: Long)
    fun deleteAll()
}
