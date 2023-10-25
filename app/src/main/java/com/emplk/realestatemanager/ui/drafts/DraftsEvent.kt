package com.emplk.realestatemanager.ui.drafts

sealed class DraftsEvent {
    object OnAddNewDraftClicked : DraftsEvent()
    data class OnDraftClicked(val id: Long) : DraftsEvent()
}
