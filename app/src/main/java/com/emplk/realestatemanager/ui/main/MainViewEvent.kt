package com.emplk.realestatemanager.ui.main

sealed class MainViewEvent {
    object DisplayAddPropertyFragment : MainViewEvent()
    data class DisplayEditPropertyFragment(val id: Long) : MainViewEvent()
    object DidNotClickedAddPropertyButtonPhone : MainViewEvent()
    object DidNotClickedAddPropertyButtonTablet : MainViewEvent()
}