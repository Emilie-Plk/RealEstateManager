package com.emplk.realestatemanager.ui.main

sealed class MainViewEvent {
    object DisplayPropertyListFragmentOnPhone : MainViewEvent()
    object DisplayPropertyListFragmentOnTablet : MainViewEvent()
    object DisplayAddPropertyFragmentOnPhone : MainViewEvent()
    object DisplayAddPropertyFragmentOnTablet : MainViewEvent()
    object DisplayEditPropertyFragmentOnPhone : MainViewEvent()
    object DisplayEditPropertyFragmentOnTablet : MainViewEvent()
    object DisplayDetailFragmentOnPhone : MainViewEvent()
    object DisplayDetailFragmentOnTablet : MainViewEvent()
    object DisplayFilterPropertiesFragmentOnPhone : MainViewEvent()
    object DisplayFilterPropertiesFragmentOnTablet : MainViewEvent()
}