package com.emplk.realestatemanager.ui.detail

sealed class DetailViewEvent {
    object DisplayDetailFragmentPhone : DetailViewEvent()
    object DisplayEditFragmentPhone : DetailViewEvent()
    object DisplayEditFragmentTablet : DetailViewEvent()
    object NavigateToMainActivity : DetailViewEvent()
}
