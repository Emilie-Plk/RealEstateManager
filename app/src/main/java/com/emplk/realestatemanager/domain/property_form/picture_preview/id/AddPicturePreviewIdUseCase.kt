package com.emplk.realestatemanager.domain.property_form.picture_preview.id

import javax.inject.Inject

class AddPicturePreviewIdUseCase @Inject constructor(
    private val picturePreviewIdRepository: PicturePreviewIdRepository,
) {
    fun invoke(picturePreviewId: Long) {
        picturePreviewIdRepository.add(picturePreviewId)
    }
}