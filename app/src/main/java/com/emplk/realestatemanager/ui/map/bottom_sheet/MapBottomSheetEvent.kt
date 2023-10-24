package com.emplk.realestatemanager.ui.map.bottom_sheet

sealed class MapBottomSheetEvent {
    object Detail : MapBottomSheetEvent()
    data class Edit(val propertyId: Long) : MapBottomSheetEvent()
}