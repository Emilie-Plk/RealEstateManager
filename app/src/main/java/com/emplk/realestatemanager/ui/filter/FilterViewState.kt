package com.emplk.realestatemanager.ui.filter

import com.emplk.realestatemanager.ui.add.address_predictions.PredictionViewState
import com.emplk.realestatemanager.ui.add.type.PropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativeText

data class FilterViewState(
    val type: String?,
    val minPrice: Int,
    val maxPrice: Int,
    val minSurface: Int,
    val maxSurface: Int,
    val propertyTypes: List<PropertyTypeViewStateItem>,
    val locationPredictions: List<PredictionViewState>,
    val location: String? = null,
    val isRadiusEditTextVisible: Boolean = false,
    val entryDate: EntryDateStatus = EntryDateStatus.NONE,
    val availableForSale: Boolean = false,
    val filterButtonText: NativeText,
    val onCancelClicked: EquatableCallback,
    val onFilterClicked: EquatableCallback
)