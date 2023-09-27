package com.emplk.realestatemanager.ui.add

import com.emplk.realestatemanager.ui.utils.NativeText

sealed class AddPropertyEvent {
    data class ShowToast(val text: NativeText) : AddPropertyEvent()
}
