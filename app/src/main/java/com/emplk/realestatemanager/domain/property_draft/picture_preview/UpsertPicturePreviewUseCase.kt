package com.emplk.realestatemanager.domain.property_draft.picture_preview

import javax.inject.Inject

class UpsertPicturePreviewUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
) {
    suspend fun invoke(picturePreviewEntity: PicturePreviewEntity, propertyFormId: Long): Long =
        picturePreviewRepository.upsert(picturePreviewEntity, propertyFormId)
}