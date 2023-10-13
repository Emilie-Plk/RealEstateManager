package com.emplk.realestatemanager.domain.property_draft.picture_preview

import com.emplk.realestatemanager.domain.property_draft.picture_preview.id.PicturePreviewIdRepository
import javax.inject.Inject

class DeletePicturePreviewUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
    private val picturePreviewIdRepository: PicturePreviewIdRepository,
    ) {
    suspend fun invoke(picturePreviewId: Long) {
        picturePreviewRepository.delete(picturePreviewId)
        picturePreviewIdRepository.delete(picturePreviewId)
    }
}
