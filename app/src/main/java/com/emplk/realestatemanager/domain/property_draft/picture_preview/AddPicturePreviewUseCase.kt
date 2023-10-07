package com.emplk.realestatemanager.domain.property_draft.picture_preview

import com.emplk.realestatemanager.domain.property_draft.GetCurrentPropertyDraftIdUseCase
import javax.inject.Inject

class AddPicturePreviewUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
    private val getCurrentPropertyDraftIdUseCase: GetCurrentPropertyDraftIdUseCase
) {
    suspend fun invoke(uriToString: String, isFeatured: Boolean): Long =
        getCurrentPropertyDraftIdUseCase.invoke()?.let {
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