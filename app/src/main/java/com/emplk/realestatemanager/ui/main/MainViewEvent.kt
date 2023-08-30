package com.emplk.realestatemanager.ui.main

sealed class MainViewEvent {
    object NavigateToAddPropertyActivity : MainViewEvent()
    object DisplayAddPropertyFragment : MainViewEvent()
    object DidNotClickedAddPropertyButtonPhone: MainViewEvent()
    object DidNotClickedAddPropertyButtonTablet: MainViewEvent()
}