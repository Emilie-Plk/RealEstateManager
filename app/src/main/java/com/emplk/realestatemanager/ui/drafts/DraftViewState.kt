package com.emplk.realestatemanager.ui.drafts

import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParams

data class DraftViewState(
    val id: Long,
    val title: String,
    val lastEditionDate: String,
    val onClick: EquatableCallbackWithParam<Long>
)