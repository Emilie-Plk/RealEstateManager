package com.emplk.realestatemanager.ui.main

sealed class MainViewAction {
    object NavigateToAddProperty : MainViewAction()
    object NavigateToDetailActivity : MainViewAction()
}