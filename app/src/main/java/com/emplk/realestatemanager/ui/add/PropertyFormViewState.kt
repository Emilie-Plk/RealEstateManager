package com.emplk.realestatemanager.ui.add

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionViewState
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentViewStateItem
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.add.picture_preview.PicturePreviewStateItem
import com.emplk.realestatemanager.ui.add.type.PropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.NativeText

data class PropertyFormViewState(
    @StringRes val propertyType: Int?,
    val addressPredictions: List<PredictionViewState>,
    val isAddressValid: Boolean,
    val address: String?,
    val price: String?,
    val surface: String?,
    val description: String?,
    val nbRooms: Int,
    val nbBathrooms: Int,
    val nbBedrooms: Int,
    val amenities: List<AmenityViewState>,
    val pictures: List<PicturePreviewStateItem>,
    val agents: List<AddPropertyAgentViewStateItem>,
    val selectedAgent: String?,
    val priceCurrencyHint: NativeText,
    @DrawableRes val currencyDrawableRes: Int,
    val surfaceUnit: NativeText,
    val isSubmitButtonEnabled: Boolean,
    val submitButtonText: NativeText,
    val isProgressBarVisible: Boolean,
    val propertyTypes: List<PropertyTypeViewStateItem>,
    val propertyCreationDate: NativeText?,
    val isSold: Boolean? = false,
    val soldStatusText: NativeText?,
    val soldDate: String? = null,
    val entryDateText: NativeText?,
    val areEditItemsVisible: Boolean,
    val isInternetEnabled: Boolean,
)