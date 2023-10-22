package com.emplk.realestatemanager.domain.property.pictures

import javax.inject.Inject

class GetPicturesIdsUseCase @Inject constructor(
    private val pictureRepository: PictureRepository,
) {
    suspend fun invoke(propertyId: Long): List<Long> =
        pictureRepository.getPicturesIds(propertyId)
}