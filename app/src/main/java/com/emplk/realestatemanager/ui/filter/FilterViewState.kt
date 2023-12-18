package com.emplk.realestatemanager.ui.filter

import androidx.annotation.StringRes
import com.emplk.realestatemanager.domain.filter.SearchedEntryDateRange
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.add.type.PropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativeText

data class FilterViewState(
    @StringRes val propertyType: Int?,
    val priceRange: NativeText,
    val minPrice: String,
    val maxPrice: String,
    val surfaceRange: NativeText,
    val minSurface: String,
    val maxSurface: String,
    val amenities: List<AmenityViewState>,
    val propertyTypes: List<PropertyTypeViewStateItem>,
    val entryDate: SearchedEntryDateRange? = SearchedEntryDateRange.ALL,
    val availableForSale: PropertySaleState? = PropertySaleState.ALL,
    val filterButtonText: NativeText,
    val isFilterButtonEnabled: Boolean,
    val onFilterClicked: EquatableCallback,
    val onCancelClicked: EquatableCallback,
)