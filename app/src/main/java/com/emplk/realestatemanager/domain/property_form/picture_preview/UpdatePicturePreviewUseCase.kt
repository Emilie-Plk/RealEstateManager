package com.emplk.realestatemanager.domain.property_form.picture_preview

import javax.inject.Inject

class UpdatePicturePreviewUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
) {
    suspend fun invoke(picturePreviewId: Long, isFeatured: Boolean?, description: String?) =
        picturePreviewRepository.update(picturePreviewId, isFeatured, description)
    }
