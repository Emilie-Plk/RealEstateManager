package com.emplk.realestatemanager.domain.property_draft.picture_preview

import com.emplk.realestatemanager.domain.property_draft.GetCurrentPropertyDraftIdUseCase
import javax.inject.Inject

class GetPicturePreviewsUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
    private val getCurrentPropertyDraftIdUseCase: GetCurrentPropertyDraftIdUseCase,
) {
    suspend fun invoke(): List<PicturePreviewEntity> =
        getCurrentPropertyDraftIdUseCase.invoke()?.let {
            picturePreviewRepository.getAll(it)
        } ?: throw Exception("Error while getting picture previews")
}