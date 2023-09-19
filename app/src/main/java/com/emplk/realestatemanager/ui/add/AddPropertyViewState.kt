package com.emplk.realestatemanager.ui.add

import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentViewStateItem
import com.emplk.realestatemanager.ui.add.picture_preview.PicturePreviewStateItem
import com.emplk.realestatemanager.ui.add.type.AddPropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.NativeText
import java.math.BigDecimal

data class AddPropertyViewState(
    val propertyType: String?,
    val address: String?,
    val price: BigDecimal,
    val surface: String?,
    val description: String?,
    val nbRooms: Int,
    val nbBathrooms: Int,
    val nbBedrooms: Int,
    val amenities: List<String>,
    val pictures: List<PicturePreviewStateItem>,
    val agent: String?,
    val priceCurrency: NativeText,
    val surfaceUnit: NativeText,
    val isAddButtonEnabled: Boolean,
    val isProgressBarVisible: Boolean,
    val propertyTypes: List<AddPropertyTypeViewStateItem>,
    val agents: List<AddPropertyAgentViewStateItem>,
)