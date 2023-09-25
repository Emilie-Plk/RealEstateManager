package com.emplk.realestatemanager.domain.property_form.picture_preview

import android.net.Uri
import com.emplk.realestatemanager.domain.property_form.GetCurrentPropertyFormIdUseCase
import javax.inject.Inject

class AddPicturePreviewUseCase @Inject constructor(
    private val picturePreviewRepository: PicturePreviewRepository,
    private val getCurrentPropertyFormIdUseCase: GetCurrentPropertyFormIdUseCase
) {
    suspend fun invoke(uri: Uri): Long {
        val pictureId = picturePreviewRepository.add(
            PicturePreviewEntity(uri = uri.toString()), getCurrentPropertyFormIdUseCase.invoke()
        )
        if (pictureId != null && pictureId > 0) return pictureId
        else throw Exception("Error while adding picture preview in database")
    }
}