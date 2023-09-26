package com.emplk.realestatemanager.domain.property_form.picture_preview

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPicturePreviewsAsFlowUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository
) {
    fun invoke(): Flow<List<PicturePreviewEntity>> = picturePreviewRepository.getAllAsFlow()
}