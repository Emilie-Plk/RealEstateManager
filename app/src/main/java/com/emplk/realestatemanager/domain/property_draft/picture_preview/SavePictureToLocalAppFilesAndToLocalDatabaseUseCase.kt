package com.emplk.realestatemanager.domain.property_draft.picture_preview

import com.emplk.realestatemanager.domain.content_resolver.SaveFileToLocalAppFilesUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.id.PicturePreviewIdRepository
import javax.inject.Inject

class SavePictureToLocalAppFilesAndToLocalDatabaseUseCase @Inject constructor(
    private val addPicturePreviewUseCase: AddPicturePreviewUseCase,
    private val picturePreviewIdRepository: PicturePreviewIdRepository,
    private val saveFileToLocalAppFilesUseCase: SaveFileToLocalAppFilesUseCase,
) {
    suspend fun invoke(stringUri: String, isFormPictureIdEmpty: Boolean): Long {
        val absolutePath = saveFileToLocalAppFilesUseCase.invoke(stringUri)
        val addedPicturePreviewId = addPicturePreviewUseCase.invoke(absolutePath, isFormPictureIdEmpty)
        picturePreviewIdRepository.add(addedPicturePreviewId)
        return addedPicturePreviewId
    }
}
