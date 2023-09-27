package com.emplk.realestatemanager.domain.map_picture

sealed class MapWrapper {
    data class Success(val map: ByteArray) : MapWrapper()
    object Error : MapWrapper()
}
