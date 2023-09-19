package com.emplk.realestatemanager.domain.property_form.picture_preview

import javax.inject.Inject

class GetPicturePreviewFlowUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
) {
    fun invoke() = picturePreviewRepository.getAsFlow()
}