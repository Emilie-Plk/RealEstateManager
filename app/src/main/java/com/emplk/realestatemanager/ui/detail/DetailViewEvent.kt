package com.emplk.realestatemanager.ui.detail

sealed class DetailViewEvent {
    object DisplayEditFragmentTablet : DetailViewEvent()
    object NavigateToMainActivity : DetailViewEvent()
}
