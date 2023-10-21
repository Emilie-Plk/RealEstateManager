package com.emplk.realestatemanager.domain.property_draft.picture_preview

import com.emplk.realestatemanager.domain.content_resolver.SaveFileToLocalAppFilesUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.id.PicturePreviewIdRepository
import javax.inject.Inject

class SavePictureToLocalAppFilesAndToLocalDatabaseUseCase @Inject constructor(
    private val addPicturePreviewUseCase: AddPicturePreviewUseCase,
    private val picturePreviewIdRepository: PicturePreviewIdRepository,
    private val saveFileToLocalAppFilesUseCase: SaveFileToLocalAppFilesUseCase,
) {
    suspend fun invoke(stringUri: String, isFormPictureIdEmpty: Boolean, propertyId: Long): Long {
        val absolutePath = saveFileToLocalAppFilesUseCase.invoke(stringUri)
        return addPicturePreviewUseCase.invoke(absolutePath, isFormPictureIdEmpty, propertyId).also { picturePreviewIdRepository.add(it) }
    }
}
