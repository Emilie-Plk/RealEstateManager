package com.emplk.realestatemanager.ui.add.picture_preview

import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.NativePhoto

sealed class PicturePreviewStateItem(val type: Type) {

    enum class Type {
        ADD_PICTURE_PREVIEW,
        EDIT_PICTURE_PREVIEW,
    }

    data class AddPropertyPicturePreview(
        val id: Long,
        val uri: String?,
        val isFeatured: Boolean,
        val description: String?,
        val onDeleteEvent: EquatableCallback,
        val onFeaturedEvent: EquatableCallbackWithParam<Boolean>,
        val onDescriptionChanged: EquatableCallbackWithParam<String>,
    ) : PicturePreviewStateItem(Type.ADD_PICTURE_PREVIEW)

    data class EditPropertyPicturePreview(
        val id: Long,
        val uri: String,
        val isFeatured: Boolean,
        val description: String?,
        val onDeleteEvent: EquatableCallback,
        val onFeaturedEvent: EquatableCallbackWithParam<Boolean>,
        val onDescriptionChanged: EquatableCallbackWithParam<String>,
    ) : PicturePreviewStateItem(Type.EDIT_PICTURE_PREVIEW)
}