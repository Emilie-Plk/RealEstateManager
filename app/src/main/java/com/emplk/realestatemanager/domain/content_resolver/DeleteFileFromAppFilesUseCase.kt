package com.emplk.realestatemanager.domain.content_resolver

import android.util.Log
import javax.inject.Inject

class DeleteFileFromAppFilesUseCase @Inject constructor(
    private val pictureFileRepository: PictureFileRepository,
) {
    suspend fun invoke(absolutePath: String) {
        pictureFileRepository.deleteFromAppFiles(absolutePath)
        Log.i(
            "Deleted picture", "Deleted picture from app file " +
                    "with absolute path: $absolutePath"
        )
    }
}