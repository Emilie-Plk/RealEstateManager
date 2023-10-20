package com.emplk.realestatemanager.ui.main

sealed class MainViewEvent {
    object DisplayPropertyList : MainViewEvent()

    data class DisplayDetailFragmentOnPhone(val propertyId: Long?) : MainViewEvent()
    data class DisplayDetailFragmentOnTablet(val propertyId: Long?) : MainViewEvent()

    object DisplayFilterPropertiesFragmentOnPhone : MainViewEvent()
    object DisplayFilterPropertiesFragmentOnTablet : MainViewEvent()

    data class NavigateToBlank(val fragmentTag: String, val propertyId: Long?) : MainViewEvent()
}