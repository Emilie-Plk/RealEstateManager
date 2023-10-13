package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.ui.utils.NativeText

sealed class AddPropertyWrapper {
    data class Success(val text: NativeText) : AddPropertyWrapper()
    data class Error(val error: NativeText) : AddPropertyWrapper()
    data class NoLatLong(val error: NativeText) : AddPropertyWrapper()
    data class LocaleError(val error: NativeText) : AddPropertyWrapper()
}