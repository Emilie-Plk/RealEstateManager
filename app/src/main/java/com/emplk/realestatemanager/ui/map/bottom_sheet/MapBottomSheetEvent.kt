package com.emplk.realestatemanager.ui.map.bottom_sheet

sealed class MapBottomSheetEvent {
    data class OnDetailClick(val propertyId: Long) : MapBottomSheetEvent()
    data class OnEditClick(val propertyId: Long) : MapBottomSheetEvent()
}