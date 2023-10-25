package com.emplk.realestatemanager.ui.map.bottom_sheet

import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParams
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText

data class PropertyMapBottomSheetViewState(
    val propertyId: Long,
    val type: String,
    val price: String,
    val surface: String,
    val rooms: NativeText,
    val bedrooms: NativeText,
    val bathrooms: NativeText,
    val description: String,
    val featuredPicture: NativePhoto,
    val onDetailClick: EquatableCallbackWithParams<Long, String>,
    val onEditClick: EquatableCallbackWithParams<Long, String>,
    val isProgressBarVisible: Boolean,
)
