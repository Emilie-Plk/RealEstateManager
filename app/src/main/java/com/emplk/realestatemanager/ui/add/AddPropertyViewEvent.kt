package com.emplk.realestatemanager.ui.add

import com.emplk.realestatemanager.ui.utils.NativeText

sealed class AddPropertyViewEvent {
    object OnAddPropertyClicked : AddPropertyViewEvent()  // TODO: Not sure what to do with this one
    data class ShowSnackBarPropertyCreated(val text: NativeText) : AddPropertyViewEvent()
}
