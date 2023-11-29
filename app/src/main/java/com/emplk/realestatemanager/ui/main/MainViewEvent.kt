package com.emplk.realestatemanager.ui.main

sealed class MainViewEvent {
    object PropertyList : MainViewEvent()

    object DetailOnPhone : MainViewEvent()
    object DetailOnTablet : MainViewEvent()

    object FilterProperties : MainViewEvent()

    object LoanSimulator : MainViewEvent()

    data class NavigateToBlank(val fragmentTag: String) : MainViewEvent()

    object NoEvent : MainViewEvent()
}