package com.emplk.realestatemanager.ui.main

sealed class MainViewEvent {
    object DisplayPropertyListFragment : MainViewEvent()
    object DisplayAddPropertyFragmentOnPhone : MainViewEvent()
    object DisplayAddPropertyFragmentOnTablet : MainViewEvent()
    data class DisplayEditPropertyFragment(val id: Long) : MainViewEvent()
    object DisplayBlankFragment : MainViewEvent()
}