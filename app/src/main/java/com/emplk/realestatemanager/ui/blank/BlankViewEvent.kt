package com.emplk.realestatemanager.ui.blank

sealed class BlankViewEvent {
    object SaveDraftDialog : BlankViewEvent()
    data class NavigateToMain(val fragmentTag: String) : BlankViewEvent()
    object OnAddNewDraftClicked : BlankViewEvent()
    data class OnDraftClicked(val id: Long) : BlankViewEvent()
}