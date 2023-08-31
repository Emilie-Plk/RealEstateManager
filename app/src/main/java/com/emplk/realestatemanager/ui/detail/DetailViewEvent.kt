package com.emplk.realestatemanager.ui.detail

sealed class DetailViewEvent {
    data class DisplayEditFragmentTablet(val id: Long) : DetailViewEvent()
    data class DisplayEditFragmentPhone(val id: Long) : DetailViewEvent()
}
