package com.emplk.realestatemanager.ui.drafts

import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText

sealed class DraftViewState(val type: Type) {

    enum class Type {
        DRAFTS,
        ADD_NEW_DRAFT,
    }

    data class DraftItem(
        val id: Long,
        val title: NativeText,
        val featuredPicture: NativePhoto,
        val featuredPictureDescription: NativeText?,
        val lastEditionDate: String,
        val onClick: EquatableCallback,
    ) : DraftViewState(Type.DRAFTS)

    data class AddNewDraftItem(
        val text: NativeText,
        val icon: NativePhoto,
        val onClick: EquatableCallback,
    ) : DraftViewState(Type.ADD_NEW_DRAFT)
}