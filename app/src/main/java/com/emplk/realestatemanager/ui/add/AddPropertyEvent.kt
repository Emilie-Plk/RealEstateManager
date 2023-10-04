package com.emplk.realestatemanager.ui.add

import com.emplk.realestatemanager.ui.utils.NativeText

sealed class AddPropertyEvent {
    data class Toast(val text: NativeText) : AddPropertyEvent()
}
