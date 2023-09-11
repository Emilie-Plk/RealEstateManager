package com.emplk.realestatemanager.ui.add.picture_preview

import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto

data class PropertyPicturePreviewStateItem(
    val id : Long,
    val uri : NativePhoto,
    val description : String,
    val isFeature : Boolean,
    val onDeleteEvent: EquatableCallback,
)