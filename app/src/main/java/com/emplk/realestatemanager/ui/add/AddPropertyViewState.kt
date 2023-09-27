package com.emplk.realestatemanager.ui.add

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionViewState
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentViewStateItem
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewStateItem
import com.emplk.realestatemanager.ui.add.picture_preview.PicturePreviewStateItem
import com.emplk.realestatemanager.ui.add.type.AddPropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.NativeText

data class AddPropertyViewState(
    val propertyType: String?,
    val addressPredictions: List<PredictionViewState>,
    val address: String?,
    val lat: String?,
    val lng: String?,
    val price: String,
    val surface: String?,
    val description: String?,
    val nbRooms: Int,
    val nbBathrooms: Int,
    val nbBedrooms: Int,
    val selectedAmenities: List<AmenityEntity>,
    val amenities: List<AmenityViewStateItem>,
    val pictures: List<PicturePreviewStateItem>,
    val agents: List<AddPropertyAgentViewStateItem>,
    val selectedAgent: String?,
    val priceCurrency: NativeText,
    val surfaceUnit: NativeText,
    val isAddButtonEnabled: Boolean,
    val isProgressBarVisible: Boolean,
    val propertyTypes: List<AddPropertyTypeViewStateItem>,
)