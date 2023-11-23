package com.emplk.realestatemanager.domain.property

import com.emplk.realestatemanager.ui.utils.NativeText

sealed class AddOrEditPropertyWrapper {
    data class Success(val text: NativeText) : AddOrEditPropertyWrapper()
    data class Error(val error: NativeText) : AddOrEditPropertyWrapper()
    data class NoInternet(val error: NativeText) : AddOrEditPropertyWrapper()
    data class NoLatLong(val error: NativeText) : AddOrEditPropertyWrapper()
    data class LocaleError(val error: NativeText) : AddOrEditPropertyWrapper()
}