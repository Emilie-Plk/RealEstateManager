package com.emplk.realestatemanager.domain.property_draft.picture_preview.id

import javax.inject.Inject

class DeletePicturePreviewIdUseCase @Inject constructor(
    private val picturePreviewIdRepository: PicturePreviewIdRepository,
) {
    fun invoke(picturePreviewId: Long) {
        picturePreviewIdRepository.delete(picturePreviewId)
    }
}