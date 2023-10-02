package com.emplk.realestatemanager.domain.property_form.picture_preview

import com.emplk.realestatemanager.domain.property_form.GetCurrentPropertyFormIdUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPicturePreviewsAsFlowUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
    private val getCurrentPropertyFormIdUseCase: GetCurrentPropertyFormIdUseCase,
) {
    suspend fun invoke(): Flow<List<PicturePreviewEntity>> {
        return getCurrentPropertyFormIdUseCase.invoke()?.let {
            picturePreviewRepository.getAllAsFlow(it)
        } ?: throw Exception("Error while getting picture previews")
    }
}