package com.emplk.realestatemanager.ui.add.save_draft

import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.NativeText

data class SaveDraftViewState(
    val isSaveMessageVisible: Boolean,
    val saveButtonEvent: EquatableCallback,
    val dialogMessage: NativeText,
    val isSubmitTitleButtonVisible: Boolean,
    val submitTitleEvent: EquatableCallbackWithParam<String>,
    val discardEvent: EquatableCallback,
    val isTitleTextInputVisible: Boolean,
)