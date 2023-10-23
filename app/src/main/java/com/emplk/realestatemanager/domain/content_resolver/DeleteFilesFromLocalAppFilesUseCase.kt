package com.emplk.realestatemanager.domain.content_resolver

import javax.inject.Inject

class DeleteFilesFromLocalAppFilesUseCase @Inject constructor(
    private val pictureFileRepository: PictureFileRepository,
) {
    suspend fun invoke(absolutePaths: List<String>) {
        pictureFileRepository.deleteFromAppFiles(absolutePaths)
    }
}