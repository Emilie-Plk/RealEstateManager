package com.emplk.realestatemanager.data.property_draft.picture_preview.id

import com.emplk.realestatemanager.domain.property_draft.picture_preview.id.PicturePreviewIdRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class PicturePreviewIdRepositoryImpl @Inject constructor() : PicturePreviewIdRepository {
    private val picturePreviewIdListMutableStateFlow: MutableStateFlow<List<Long>> = MutableStateFlow(emptyList())

    override fun add(picturePreviewId: Long) {
        picturePreviewIdListMutableStateFlow.tryEmit(picturePreviewIdListMutableStateFlow.value + picturePreviewId)
    }

    override fun addAll(picturePreviewIds: List<Long>) {
        picturePreviewIdListMutableStateFlow.tryEmit(picturePreviewIdListMutableStateFlow.value + picturePreviewIds)
    }

    override fun getAllAsFlow(): Flow<List<Long>> = picturePreviewIdListMutableStateFlow

    override fun delete(picturePreviewId: Long) {
        picturePreviewIdListMutableStateFlow.tryEmit(picturePreviewIdListMutableStateFlow.value - picturePreviewId)
    }

    override fun deleteAll() {
        picturePreviewIdListMutableStateFlow.tryEmit(emptyList())
    }
}