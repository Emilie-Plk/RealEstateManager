package com.emplk.realestatemanager.domain.property_draft.picture_preview

import com.emplk.realestatemanager.domain.property_draft.GetCurrentDraftIdUseCase
import javax.inject.Inject

class AddPicturePreviewUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
    private val getCurrentDraftIdUseCase: GetCurrentDraftIdUseCase
) {
    suspend fun invoke(uriToString: String, isFeatured: Boolean): Long =
        getCurrentDraftIdUseCase.invoke()?.let {
            picturePreviewRepository.add(
                PicturePreviewEntity(
                    id = 0,
                    uri = uriToString,
                    description = null,
                    isFeatured = isFeatured,
                ), it
            )
        } ?: throw Exception("Error while adding picture preview")
}