package com.emplk.realestatemanager.ui.blank

sealed class BlankViewEvent {
    object SaveDraftDialog : BlankViewEvent()
    object TitleDraftDialog : BlankViewEvent()
    data class NavigateToMain(val fragmentTag: String) : BlankViewEvent()
}