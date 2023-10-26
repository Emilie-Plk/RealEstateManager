package com.emplk.realestatemanager.ui.add.draft_dialog

sealed class SaveDraftEvent {
    object Save : SaveDraftEvent()
    object SubmitTitle : SaveDraftEvent()
    object Discard : SaveDraftEvent()
}
