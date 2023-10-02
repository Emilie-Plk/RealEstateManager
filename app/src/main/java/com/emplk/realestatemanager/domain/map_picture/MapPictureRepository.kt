package com.emplk.realestatemanager.domain.map_picture

interface MapPictureRepository {
    suspend fun getMapPicture(
        latitude: String,
        longitude: String,
        zoom: String,
        size: String,
        markers: String,
    ): MapWrapper
}
