package com.emplk.realestatemanager.domain.property_form.picture_preview

import com.emplk.realestatemanager.domain.property_form.GetCurrentPropertyFormIdUseCase
import javax.inject.Inject

class DeleteAllPicturePreviewUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
    private val getCurrentPropertyFormIdUseCase: GetCurrentPropertyFormIdUseCase,
) {
    suspend fun invoke() = getCurrentPropertyFormIdUseCase.invoke()?.let { picturePreviewRepository.delete(it) }
}