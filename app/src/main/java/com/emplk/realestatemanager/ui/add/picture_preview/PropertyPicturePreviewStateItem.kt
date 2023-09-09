package com.emplk.realestatemanager.ui.add.picture_preview

import com.emplk.realestatemanager.ui.utils.EquatableCallback

data class PropertyPicturePreviewStateItem(
    val id : Long,
    val uri : String,
    val description : String,
    val isFeature : Boolean,
    val onDeleteEvent: EquatableCallback,
)