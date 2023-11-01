package com.emplk.realestatemanager.ui.main

sealed class MainViewEvent {
    object PropertyList : MainViewEvent()

    data class DetailFragmentOnPhone(val propertyId: Long?) : MainViewEvent()
    data class DetailFragmentOnTablet(val propertyId: Long?) : MainViewEvent()

    object FilterPropertiesFragmentOnPhone : MainViewEvent()
    object FilterPropertiesFragmentOnTablet : MainViewEvent()

    object LoanSimulator : MainViewEvent()

    data class NavigateToBlank(val fragmentTag: String, val propertyId: Long?) : MainViewEvent()
}