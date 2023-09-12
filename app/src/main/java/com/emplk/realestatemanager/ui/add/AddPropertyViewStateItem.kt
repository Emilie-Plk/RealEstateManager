package com.emplk.realestatemanager.ui.add

import com.emplk.realestatemanager.domain.pictures.PictureEntity
import java.math.BigDecimal

data class AddPropertyViewStateItem(
    val propertyType: String,
    val address: String,
    val price: BigDecimal,
    val surface: Int,
    val description: String,
    val nbRooms: Int,
    val nbBathrooms: Int,
    val nbBedrooms: Int,
    val amenities: List<String>,
    val agentName : AddPropertyAgentViewStateItem,
)