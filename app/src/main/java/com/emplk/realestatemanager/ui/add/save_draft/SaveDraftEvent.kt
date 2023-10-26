package com.emplk.realestatemanager.ui.add.save_draft

sealed class SaveDraftEvent {
    object Save : SaveDraftEvent()
    object SubmitTitle : SaveDraftEvent()
    object Discard : SaveDraftEvent()
}
