package com.emplk.realestatemanager.domain.property_draft.picture_preview

import javax.inject.Inject

class DeletePicturePreviewByIdUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository
) {
    suspend fun invoke(picturePreviewId: Long) = picturePreviewRepository.delete(picturePreviewId)
}