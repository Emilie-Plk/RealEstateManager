package com.emplk.realestatemanager.ui.map

sealed class MapEvent {
    data class OnMarkerClicked(val propertyId: Long) : MapEvent()
}