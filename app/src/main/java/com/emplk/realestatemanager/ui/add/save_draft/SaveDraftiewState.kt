package com.emplk.realestatemanager.ui.add.save_draft

import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam

data class SaveDraftViewState(
    val isSaveMessageVisible: Boolean,
    val saveButtonEvent: EquatableCallback,
    val isSubmitTitleButtonVisible: Boolean,
    val submitTitleEvent: EquatableCallbackWithParam<String>,
    val discardEvent: EquatableCallback,
    val isTitleTextInputVisible: Boolean,
)