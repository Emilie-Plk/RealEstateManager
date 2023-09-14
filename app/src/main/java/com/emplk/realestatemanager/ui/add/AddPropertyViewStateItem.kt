package com.emplk.realestatemanager.ui.add

import com.emplk.realestatemanager.ui.add.picture_preview.PicturePreviewStateItem

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
    val pictures: List<PicturePreviewStateItem>,
    val agent: String,
)