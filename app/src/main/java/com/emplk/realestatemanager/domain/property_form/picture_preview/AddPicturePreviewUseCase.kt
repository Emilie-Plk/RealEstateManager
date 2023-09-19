package com.emplk.realestatemanager.domain.property_form.picture_preview

import javax.inject.Inject

class AddPicturePreviewUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
) {
    suspend fun invoke(picturePreviewEntity: PicturePreviewEntity, propertyFormId: Long): Long =
        picturePreviewRepository.add(picturePreviewEntity, propertyFormId)
}