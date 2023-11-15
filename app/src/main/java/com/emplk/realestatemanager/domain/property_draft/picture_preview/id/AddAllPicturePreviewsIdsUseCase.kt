package com.emplk.realestatemanager.domain.property_draft.picture_preview.id

import com.emplk.realestatemanager.domain.property_draft.picture_preview.PicturePreviewRepository
import javax.inject.Inject

class AddAllPicturePreviewsIdsUseCase @Inject constructor(
    private val picturePreviewIdRepository: PicturePreviewIdRepository,
) {
    fun invoke(picturePreviewsIds: List<Long>) {
        picturePreviewIdRepository.addAll(picturePreviewsIds)
    }
}