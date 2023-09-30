package com.emplk.realestatemanager.domain.map_picture

import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class GetMapPictureUseCase @Inject constructor(
    private val mapPictureRepository: MapPictureRepository
) {

    suspend fun invoke(
        latLng: LatLng
    ): MapWrapper = mapPictureRepository.getMapPicture(
        latitude = latLng.latitude.toString(),
        longitude = latLng.longitude.toString(),
        zoom = 16,
        size = "480x320",
        markers = "color:red|${latLng.latitude},${latLng.longitude}"
    )
}