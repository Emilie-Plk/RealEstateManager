package com.emplk.realestatemanager.domain.content_resolver

import javax.inject.Inject

class DeleteFileFromLocalAppFilesUseCase @Inject constructor(
    private val pictureFileRepository: PictureFileRepository,
) {
    suspend fun invoke(absolutePath: String) {
        pictureFileRepository.deleteFromAppFiles(absolutePath)
    }
}