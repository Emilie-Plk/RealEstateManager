package com.emplk.realestatemanager.domain.property_draft.picture_preview

import com.emplk.realestatemanager.domain.property_draft.GetCurrentDraftIdUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPicturePreviewsAsFlowUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
    private val getCurrentDraftIdUseCase: GetCurrentDraftIdUseCase,
) {
    suspend fun invoke(): Flow<List<PicturePreviewEntity>> {
        return getCurrentDraftIdUseCase.invoke()?.let {
            picturePreviewRepository.getAllAsFlow(it)
        } ?: throw Exception("Error while getting picture previews")
    }
}