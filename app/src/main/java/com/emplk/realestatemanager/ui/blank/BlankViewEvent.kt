package com.emplk.realestatemanager.ui.blank

sealed class BlankViewEvent {
    object DisplayDraftDialog : BlankViewEvent()
    object NavigateToMain : BlankViewEvent()
}