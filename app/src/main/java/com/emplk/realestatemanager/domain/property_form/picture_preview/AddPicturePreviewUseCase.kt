package com.emplk.realestatemanager.domain.property_form.picture_preview

import com.emplk.realestatemanager.domain.property_form.GetCurrentPropertyFormIdUseCase
import javax.inject.Inject

class AddPicturePreviewUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
    private val getCurrentPropertyFormIdUseCase: GetCurrentPropertyFormIdUseCase
) {
    suspend fun invoke(uriToString: String): Long = picturePreviewRepository.add(
        PicturePreviewEntity(
            id = 0,
            uri = uriToString,
            description = null,
            isFeatured = false
        ), getCurrentPropertyFormIdUseCase.invoke()
    ) ?: throw Exception("Error while adding picture preview")
}