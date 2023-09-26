package com.emplk.realestatemanager.domain.property_form.picture_preview.id

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPicturePreviewIdsAsFlowUseCase @Inject constructor(private val picturePreviewIdRepository: PicturePreviewIdRepository) {
    fun invoke(): Flow<List<Long>> = picturePreviewIdRepository.getAllAsFlow()
}