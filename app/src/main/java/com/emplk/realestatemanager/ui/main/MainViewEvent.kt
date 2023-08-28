package com.emplk.realestatemanager.ui.main

sealed class MainViewEvent {
    object NavigateToAddPropertyActivity : MainViewEvent()
    object DoNothingForTheMoment : MainViewEvent()
    object NavigateToDetailActivity : MainViewEvent()
}