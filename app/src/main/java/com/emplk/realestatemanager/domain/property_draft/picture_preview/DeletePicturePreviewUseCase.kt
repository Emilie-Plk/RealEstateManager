package com.emplk.realestatemanager.domain.property_draft.picture_preview

import com.emplk.realestatemanager.domain.content_resolver.DeleteFileFromLocalAppFilesUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.id.PicturePreviewIdRepository
import javax.inject.Inject

class DeletePicturePreviewUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
    private val picturePreviewIdRepository: PicturePreviewIdRepository,
    private val deleteFileFromLocalAppFilesUseCase: DeleteFileFromLocalAppFilesUseCase,
) {
    suspend fun invoke(picturePreviewId: Long, picturePath: String) {
        picturePreviewRepository.delete(picturePreviewId)
        picturePreviewIdRepository.delete(picturePreviewId)
        deleteFileFromLocalAppFilesUseCase.invoke(picturePath)
    }
}
