package com.emplk.realestatemanager.ui.add

import com.emplk.realestatemanager.ui.utils.NativeText

sealed class FormEvent {
    object Loading : FormEvent()
    object Form : FormEvent()
    data class Toast(val text: NativeText) : FormEvent()
    data class Error(val errorMessage: String) : FormEvent()
}
