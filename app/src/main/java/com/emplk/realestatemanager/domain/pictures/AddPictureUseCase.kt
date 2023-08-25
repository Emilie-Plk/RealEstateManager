package com.emplk.realestatemanager.domain.pictures

import com.emplk.realestatemanager.domain.entities.PictureEntity
import javax.inject.Inject

class AddPictureUseCase @Inject constructor(
    private val pictureRepository: PictureRepository,
) {
    suspend fun invoke(picture: PictureEntity) {
        pictureRepository.addPicture(picture)
    }
}