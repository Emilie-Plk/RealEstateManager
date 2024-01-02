package com.emplk.realestatemanager.domain.content_resolver

import android.util.Log
import javax.inject.Inject

class SaveFileToLocalAppFilesUseCase @Inject constructor(private val pictureFileRepository: PictureFileRepository) {
    companion object {
        private const val PROPERTY_PICTURE_PREFIX = "property_picture_"
    }

    suspend fun invoke(stringUri: String): String? =
        pictureFileRepository.saveToAppFiles(stringUri, PROPERTY_PICTURE_PREFIX).also {
            Log.i("Saved picture", "Saved picture (string uri: $stringUri) to app file to absolute path: $it")
        }
}