package com.emplk.realestatemanager.domain.property_form.picture_preview

import javax.inject.Inject

class UpsertPicturePreviewUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
) {
    suspend fun invoke(picturePreviewEntity: PicturePreviewEntity): Long =
        picturePreviewRepository.upsert(picturePreviewEntity)
}