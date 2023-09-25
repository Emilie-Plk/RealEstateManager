package com.emplk.realestatemanager.domain.property_form.picture_preview

import javax.inject.Inject

class DeletePicturePreviewUseCase @Inject constructor(private val picturePreviewRepository: PicturePreviewRepository) {

    suspend fun invoke(picturePreviewId: Long) {
        picturePreviewRepository.delete(picturePreviewId)
    }
}