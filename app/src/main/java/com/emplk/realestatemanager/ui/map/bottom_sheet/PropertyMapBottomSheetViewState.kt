package com.emplk.realestatemanager.ui.map.bottom_sheet

import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText

data class PropertyMapBottomSheetViewState(
    val propertyId: Long,
    val type: String,
    val price: String,
    val surface: String,
    val amenities: List<AmenityViewState>,
    val rooms: NativeText,
    val bedrooms: NativeText,
    val bathrooms: NativeText,
    val description: String,
    val featuredPicture: NativePhoto,
    val onDetailClick: EquatableCallback,
    val onEditClick: EquatableCallback,
    val isProgressBarVisible: Boolean,
)
