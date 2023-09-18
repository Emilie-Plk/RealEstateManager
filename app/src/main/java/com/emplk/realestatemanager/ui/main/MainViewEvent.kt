package com.emplk.realestatemanager.ui.main

sealed class MainViewEvent {
    object DisplayPropertyList : MainViewEvent()

    object DisplayDetailFragmentOnPhone : MainViewEvent()
    object DisplayDetailFragmentOnTablet : MainViewEvent()

    object DisplayFilterPropertiesFragmentOnPhone : MainViewEvent()
    object DisplayFilterPropertiesFragmentOnTablet : MainViewEvent()

    data class NavigateToBlank(val fragmentTag: String) : MainViewEvent()
}