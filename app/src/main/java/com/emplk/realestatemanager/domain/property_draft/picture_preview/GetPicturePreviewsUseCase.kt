package com.emplk.realestatemanager.domain.property_draft.picture_preview

import com.emplk.realestatemanager.domain.property_draft.GetCurrentDraftIdUseCase
import javax.inject.Inject

class GetPicturePreviewsUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
    private val getCurrentDraftIdUseCase: GetCurrentDraftIdUseCase,
) {
    suspend fun invoke(): List<PicturePreviewEntity> =
        getCurrentDraftIdUseCase.invoke()?.let {
            picturePreviewRepository.getAll(it)
        } ?: throw Exception("Error while getting picture previews")
}