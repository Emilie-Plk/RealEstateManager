package com.emplk.realestatemanager.ui.blank

sealed class BlankViewEvent {
    object DisplayDraftDialog : BlankViewEvent()
    data class NavigateToMain(val fragmentTag: String) : BlankViewEvent()
}