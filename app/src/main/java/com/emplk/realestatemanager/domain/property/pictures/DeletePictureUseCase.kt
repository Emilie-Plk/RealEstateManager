package com.emplk.realestatemanager.domain.property.pictures

import javax.inject.Inject

class DeletePictureUseCase @Inject constructor(
    private val pictureRepository: PictureRepository,
) {
    suspend fun invoke(pictureId: Long) {
        pictureRepository.delete(pictureId)
    }
}