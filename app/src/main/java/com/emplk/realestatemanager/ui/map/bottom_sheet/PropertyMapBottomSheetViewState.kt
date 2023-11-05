package com.emplk.realestatemanager.ui.map.bottom_sheet

import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
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
    val onDetailClick: EquatableCallbackWithParam<String>,
    val onEditClick: EquatableCallbackWithParam<String>,
    val isProgressBarVisible: Boolean,
)
