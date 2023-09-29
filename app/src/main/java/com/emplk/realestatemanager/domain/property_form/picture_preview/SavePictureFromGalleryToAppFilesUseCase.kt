package com.emplk.realestatemanager.domain.property_form.picture_preview

import android.net.Uri
import javax.inject.Inject

class SavePictureFromGalleryToAppFilesUseCase @Inject constructor(private val picturePreviewRepository: PicturePreviewRepository) {
suspend fun invoke(uri: Uri): String? = picturePreviewRepository.saveToAppFiles(uri)
}
