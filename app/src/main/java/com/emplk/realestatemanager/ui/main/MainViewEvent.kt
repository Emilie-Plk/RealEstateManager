package com.emplk.realestatemanager.ui.main

sealed class MainViewEvent {
    object PropertyList : MainViewEvent()

    object DetailFragmentOnPhone : MainViewEvent()
    object DetailFragmentOnTablet : MainViewEvent()

    object FilterPropertiesFragmentOnPhone : MainViewEvent()
    object FilterPropertiesFragmentOnTablet : MainViewEvent()

    object LoanSimulator : MainViewEvent()

    data class NavigateToBlank(val fragmentTag: String) : MainViewEvent()
}