package com.emplk.realestatemanager.ui.add

import com.emplk.realestatemanager.ui.AddPropertyPictureStateItem
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentViewStateItem
import com.emplk.realestatemanager.ui.utils.NativeText
import java.math.BigDecimal

data class AddPropertyViewStateItem(
    val propertyType: String,
    val address: String,
    val price: String,
    val surface: String,
    val description: String,
    val nbRooms: String,
    val nbBathrooms: String,
    val nbBedrooms: String,
    val amenities: List<String>,
    val pictures: List<AddPropertyPictureStateItem>,
    val agent : AddPropertyAgentViewStateItem,
)