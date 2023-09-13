package com.emplk.realestatemanager.ui.add

import com.emplk.realestatemanager.ui.utils.NativeText

sealed class AddPropertyViewEvent {
    object OnAddPropertyClicked : AddPropertyViewEvent()
    data class ShowSnackBarPropertyCreated(val text : NativeText) : AddPropertyViewEvent()
}
