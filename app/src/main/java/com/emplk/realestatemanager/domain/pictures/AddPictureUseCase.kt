package com.emplk.realestatemanager.domain.pictures

import com.emplk.realestatemanager.data.picture.PictureDtoEntity
import javax.inject.Inject

class AddPictureUseCase @Inject constructor(
    private val pictureRepository: PictureRepository,
) {
    suspend fun invoke(picture: PictureDtoEntity) {
        pictureRepository.addPicture(picture)
    }
}