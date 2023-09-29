package com.emplk.realestatemanager.domain.map_picture

import javax.inject.Inject

class GetMapPictureUseCase @Inject constructor(
    private val mapPictureRepository: MapPictureRepository
) {

    suspend fun invoke(
        latitude: String,
        longitude: String,
    ) : MapWrapper = mapPictureRepository.getMapPicture(
        latitude = latitude,
        longitude = longitude,
        zoom = 16,
        size = "480x320",
        markers = "color:red|$latitude,$longitude"
    )
}