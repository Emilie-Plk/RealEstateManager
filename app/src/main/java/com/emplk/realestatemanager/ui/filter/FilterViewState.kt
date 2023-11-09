package com.emplk.realestatemanager.ui.filter

import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.add.type.PropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativeText

data class FilterViewState(
    val type: String?,
    val minPrice: Int,
    val maxPrice: Int,
    val minSurface: Int,
    val maxSurface: Int,
    val amenities: List<AmenityViewState>,
    val propertyTypes: List<PropertyTypeViewStateItem>,
    val entryDate: EntryDateState = EntryDateState.NONE,
    val availableForSale: PropertySaleState = PropertySaleState.ALL,
    val filterButtonText: NativeText,
    val onCancelClicked: EquatableCallback,
    val onFilterClicked: EquatableCallback
)