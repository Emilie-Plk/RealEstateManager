package com.emplk.realestatemanager.ui.drafts

import com.emplk.realestatemanager.ui.utils.EquatableCallback

data class DraftViewStateItem(
    val id: Long,
    val title: String,
    val lastEditionDate: String,
    val onClick: EquatableCallback,
)