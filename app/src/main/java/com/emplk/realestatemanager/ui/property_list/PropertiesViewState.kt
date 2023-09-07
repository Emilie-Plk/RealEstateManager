package com.emplk.realestatemanager.ui.property_list

import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText

sealed class PropertiesViewState(val type: Type) {

    enum class Type {
        PROPERTY,
        EMPTY_STATE,
    }

    object EmptyState : PropertiesViewState(Type.EMPTY_STATE)

    data class Properties(
        val id: Long,
        val propertyType: String,
        val featuredPicture: NativePhoto,
        val address: String,
        val price: NativeText,
        val isSold: Boolean,
        val room: String,
        val bathroom: String,
        val bedroom: String,
        val surface: NativeText,
        val onClickEvent: EquatableCallback,
    ) : PropertiesViewState(Type.PROPERTY)
}