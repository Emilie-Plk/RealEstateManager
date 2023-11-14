package com.emplk.realestatemanager.ui.main

data class MainViewState(
    val isAddFabVisible: Boolean,
    val isFilterAppBarButtonVisible: Boolean,
    val isResetFilterAppBarButtonVisible: Boolean,
    val isAddAppBarButtonVisible: Boolean,
    val subtitle: String?,
)