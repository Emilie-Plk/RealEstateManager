package com.emplk.realestatemanager.ui.map

sealed class MapEvent {
    object OnMarkerClicked : MapEvent()
}