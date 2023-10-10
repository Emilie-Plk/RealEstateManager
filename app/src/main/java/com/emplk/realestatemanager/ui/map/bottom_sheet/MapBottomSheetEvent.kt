package com.emplk.realestatemanager.ui.map.bottom_sheet

sealed class MapBottomSheetEvent {
    data class OnDetailClick(val fragmentTag: String) : MapBottomSheetEvent()
    data class OnEditClick(val fragmentTag: String) : MapBottomSheetEvent()
}