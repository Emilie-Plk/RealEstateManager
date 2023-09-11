package com.emplk.realestatemanager.ui.add.picture_preview

import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto

sealed class PicturePreviewStateItem(val type: Type) {

    enum class Type {
        ADD_PICTURE_PREVIEW,
        EDIT_PICTURE_PREVIEW,
    }

    data class AddPropertyPicturePreview(
        val id: Long,
        val uri: NativePhoto,
        val description: String,
        val isFeatured: Boolean,
        val onDeleteEvent: EquatableCallback,
    ) : PicturePreviewStateItem(Type.ADD_PICTURE_PREVIEW)

    data class EditPropertyPicturePreview(
        val id: Long,
        val uri: NativePhoto,
        val description: String,
        val isFeatured: Boolean,
        val onDeleteEvent: EquatableCallback,
    ) : PicturePreviewStateItem(Type.EDIT_PICTURE_PREVIEW)
}