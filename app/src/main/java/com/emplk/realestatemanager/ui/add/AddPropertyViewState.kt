package com.emplk.realestatemanager.ui.add

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionViewState
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentViewStateItem
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.add.picture_preview.PicturePreviewStateItem
import com.emplk.realestatemanager.ui.add.type.AddPropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.NativeText

data class AddPropertyViewState(
    // TODO: generalize to be used for both Add/EditProperty? (PropertyFormViewState?)
    val propertyType: String?,
    val addressPredictions: List<PredictionViewState>,
    val address: String?,
    val price: String?,
    val surface: String?,
    val description: String?,
    val nbRooms: Int,
    val nbBathrooms: Int,
    val nbBedrooms: Int,
    val selectedAmenities: List<AmenityEntity>,
    val amenities: List<AmenityViewState>,
    val pictures: List<PicturePreviewStateItem>,
    val agents: List<AddPropertyAgentViewStateItem>,
    val selectedAgent: String?,
    val priceCurrency: NativeText,
    val surfaceUnit: NativeText,
    val isAddButtonEnabled: Boolean,
    val isProgressBarVisible: Boolean,
    val propertyTypes: List<AddPropertyTypeViewStateItem>,
)