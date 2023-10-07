package com.emplk.realestatemanager.domain.property_draft.picture_preview.id

import javax.inject.Inject

class DeleteAllPicturePreviewIdsUseCase @Inject constructor(
    private val picturePreviewIdRepository: PicturePreviewIdRepository,
) {
    fun invoke() {
        picturePreviewIdRepository.deleteAll()
    }
}