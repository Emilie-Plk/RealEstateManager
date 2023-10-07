package com.emplk.realestatemanager.domain.property_draft.picture_preview

import com.emplk.realestatemanager.domain.property_draft.GetCurrentPropertyDraftIdUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPicturePreviewsAsFlowUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
    private val getCurrentPropertyDraftIdUseCase: GetCurrentPropertyDraftIdUseCase,
) {
    suspend fun invoke(): Flow<List<PicturePreviewEntity>> {
        return getCurrentPropertyDraftIdUseCase.invoke()?.let {
            picturePreviewRepository.getAllAsFlow(it)
        } ?: throw Exception("Error while getting picture previews")
    }
}