package com.emplk.realestatemanager.domain.map_picture

sealed class MapWrapper {
    data class Success(val mapPicture: String) : MapWrapper()
    object Error : MapWrapper()
}
