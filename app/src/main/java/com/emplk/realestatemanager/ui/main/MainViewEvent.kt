package com.emplk.realestatemanager.ui.main

sealed class MainViewEvent {
    object DisplayPropertyListFragment : MainViewEvent()
    object DisplayAddPropertyFragmentOnPhone : MainViewEvent()
    object DisplayAddPropertyFragmentOnTablet : MainViewEvent()
    object DisplayBlankFragment : MainViewEvent()
    object DisplayDetailFragment : MainViewEvent()
}