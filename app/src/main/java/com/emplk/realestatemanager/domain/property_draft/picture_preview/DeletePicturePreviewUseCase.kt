package com.emplk.realestatemanager.domain.property_draft.picture_preview

import com.emplk.realestatemanager.domain.content_resolver.DeleteFileFromAppFilesUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.id.PicturePreviewIdRepository
import javax.inject.Inject

class DeletePicturePreviewUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
    private val picturePreviewIdRepository: PicturePreviewIdRepository,
    private val deleteFileFromAppFilesUseCase: DeleteFileFromAppFilesUseCase,
) {
    suspend fun invoke(picturePreviewId: Long, picturePath: String) {
        picturePreviewRepository.delete(picturePreviewId)
        picturePreviewIdRepository.delete(picturePreviewId)
        deleteFileFromAppFilesUseCase.invoke(picturePath)
    }
}
