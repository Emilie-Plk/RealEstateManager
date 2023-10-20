package com.emplk.realestatemanager.ui.add

import com.emplk.realestatemanager.ui.utils.NativeText

sealed class FormEvent {
    data class Toast(val text: NativeText) : FormEvent()
    data class Error(val errorMessage: String) : FormEvent()
}
