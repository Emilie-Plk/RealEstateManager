package com.emplk.realestatemanager.ui.filter

import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativeText

data class FilterViewState(
    val type: String? = null,
    val minPrice: Int = 0,
    val maxPrice: Int = 0,
    val minSurface: Int = 0,
    val maxSurface: Int = 0,
    val location: String? = null,
    val entryDate: EntryDateStatus = EntryDateStatus.NONE,
    val availableForSale: Boolean = false,
    val filterButtonText: NativeText,
    val onCancelClicked: EquatableCallback,
    val onFilterClicked: EquatableCallback
)