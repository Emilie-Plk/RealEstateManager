package com.emplk.realestatemanager.domain.property_draft.picture_preview

import javax.inject.Inject

class AddPicturePreviewUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
) {
    suspend fun invoke(uriToString: String, isFeatured: Boolean, id: Long): Long =
        picturePreviewRepository.add(
            PicturePreviewEntity(
                id = 0,
                uri = uriToString,
                description = null,
                isFeatured = isFeatured,
            ), id
        )
}