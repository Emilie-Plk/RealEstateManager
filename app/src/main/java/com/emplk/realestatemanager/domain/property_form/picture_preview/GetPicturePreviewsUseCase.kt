package com.emplk.realestatemanager.domain.property_form.picture_preview

import com.emplk.realestatemanager.domain.property_form.GetCurrentPropertyFormIdUseCase
import javax.inject.Inject

class GetPicturePreviewsUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
    private val getCurrentPropertyFormIdUseCase: GetCurrentPropertyFormIdUseCase,
) {
    suspend fun invoke(): List<PicturePreviewEntity> =
        getCurrentPropertyFormIdUseCase.invoke()?.let {
            picturePreviewRepository.getAll(it)
        } ?: throw Exception("Error while getting picture previews")
}