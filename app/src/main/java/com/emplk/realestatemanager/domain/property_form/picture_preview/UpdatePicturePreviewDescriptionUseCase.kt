package com.emplk.realestatemanager.domain.property_form.picture_preview

import javax.inject.Inject

class UpdatePicturePreviewDescriptionUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
) {
    suspend fun invoke(pictureId: Long, newDescription: String?) =
        picturePreviewRepository.updateDescription(pictureId, newDescription)
}