package com.emplk.realestatemanager.domain.property_form

import com.emplk.realestatemanager.domain.property_form.picture_preview.id.PicturePreviewIdRepository
import javax.inject.Inject

class ClearPropertyFormUseCase @Inject constructor(
    private val deleteTemporaryPropertyFormUseCase: DeleteTemporaryPropertyFormUseCase,
    private val picturePreviewIdRepository: PicturePreviewIdRepository,
) {
    suspend fun invoke() {
        deleteTemporaryPropertyFormUseCase.invoke()
        picturePreviewIdRepository.deleteAll()
    }
}