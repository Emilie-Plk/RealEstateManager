package com.emplk.realestatemanager.ui.drafts

import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText

sealed class DraftViewStateItem(val type: Type) {

    enum class Type {
        DRAFTS,
        ADD_NEW_DRAFT,
    }

    data class Drafts(
        val id: Long,
        val title: NativeText,
        val featuredPicture: NativePhoto,
        val featuredPictureDescription: NativeText?,
        val lastEditionDate: String,
        val onClick: EquatableCallback,
    ) : DraftViewStateItem(Type.DRAFTS)

    data class AddNewDraft(
        val onClick: EquatableCallback,
    ) : DraftViewStateItem(Type.ADD_NEW_DRAFT)
}