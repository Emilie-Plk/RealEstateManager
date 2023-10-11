package com.emplk.realestatemanager.ui.edit

import com.emplk.realestatemanager.ui.utils.NativeText

sealed class EditPropertyEvent {
    data class Toast(val text: NativeText) : EditPropertyEvent()
}
