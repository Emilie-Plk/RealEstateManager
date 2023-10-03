package com.emplk.realestatemanager.ui.map.bottom_sheet

import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.NativePhoto

data class PropertyMapBottomSheetViewState(
    val propertyId: Long,
    val type: String,
    val price: String,
    val surface: String,
    // val amenities: List<String>,
    val rooms: String,
    val bedrooms: String,
    val bathrooms: String,
    val description: String,
    val featuredPicture: NativePhoto,
    val onDetailClick: EquatableCallbackWithParam<Long>,
    val onEditClick: EquatableCallbackWithParam<Long>,
    val isProgressBarVisible: Boolean,
)
