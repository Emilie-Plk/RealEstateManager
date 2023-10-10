package com.emplk.realestatemanager.ui.map.bottom_sheet

import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import java.math.BigDecimal

data class PropertyMapBottomSheetViewState(
    val propertyId: Long,
    val type: String,
    val price: String,
    val surface: String,
    // val amenities: List<String>,
    val rooms: NativeText,
    val bedrooms: NativeText,
    val bathrooms: NativeText,
    val description: String,
    val featuredPicture: NativePhoto,
    val onDetailClick: EquatableCallbackWithParam<Long>,
    val onEditClick: EquatableCallbackWithParam<Long>,
    val isProgressBarVisible: Boolean,
)
