package com.emplk.realestatemanager.ui.map

import com.emplk.realestatemanager.ui.utils.NativeText

sealed class MapEvent {
    object OnMarkerClicked : MapEvent()
    data class Toast(val message: NativeText?) : MapEvent()
}