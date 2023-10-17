package com.emplk.realestatemanager.domain.property_draft.picture_preview

import javax.inject.Inject

class GetPicturePreviewsUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
) {
    suspend fun invoke(id: Long): List<PicturePreviewEntity> =
        picturePreviewRepository.getAll(id)
}