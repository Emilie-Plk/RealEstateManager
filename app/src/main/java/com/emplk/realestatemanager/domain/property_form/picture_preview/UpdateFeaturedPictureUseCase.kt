package com.emplk.realestatemanager.domain.property_form.picture_preview

import javax.inject.Inject

class UpdateFeaturedPictureUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
) {
    suspend fun invoke(pictureId: Long, newFeaturedPicture: Boolean) =
        picturePreviewRepository.updateFeaturedPicture(pictureId, newFeaturedPicture)
}