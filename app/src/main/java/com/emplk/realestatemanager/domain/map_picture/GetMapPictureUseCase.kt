package com.emplk.realestatemanager.domain.map_picture

import com.google.android.gms.maps.model.LatLng
import javax.inject.Inject

class GetMapPictureUseCase @Inject constructor(
    private val mapPictureRepository: MapPictureRepository
) {

    companion object {
        private const val ZOOM = "16"
        private const val SIZE = "480x320"
        private const val MARKER_COLOR = "color:red|"
    }

    suspend fun invoke(
        latLng: LatLng
    ): MapWrapper = mapPictureRepository.getMapPicture(
        latitude = latLng.latitude.toString(),
        longitude = latLng.longitude.toString(),
        zoom = ZOOM,
        size = SIZE,
        markers = MARKER_COLOR + "${latLng.latitude},${latLng.longitude}"
    )
}