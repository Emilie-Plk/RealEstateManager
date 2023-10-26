package com.emplk.realestatemanager.ui.drafts

import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativeText

data class DraftViewStateItem(
    val id: Long,
    val title: NativeText,
    val lastEditionDate: String,
    val onClick: EquatableCallback,
)