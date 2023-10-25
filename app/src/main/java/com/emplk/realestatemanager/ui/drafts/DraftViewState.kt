package com.emplk.realestatemanager.ui.drafts

import com.emplk.realestatemanager.ui.utils.EquatableCallback

data class DraftViewState (
    val drafts: List<DraftViewStateItem>,
    val onAddNewDraftClicked: EquatableCallback,
)