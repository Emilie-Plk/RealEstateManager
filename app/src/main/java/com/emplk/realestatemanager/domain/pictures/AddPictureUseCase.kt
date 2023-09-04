package com.emplk.realestatemanager.domain.pictures

import javax.inject.Inject

class AddPictureUseCase @Inject constructor(
    private val pictureRepository: PictureRepository,
) {
    suspend fun invoke(picture: PictureEntity) {
        pictureRepository.addPicture(picture)
    }
}