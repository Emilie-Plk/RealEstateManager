package com.emplk.realestatemanager.ui.list

import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText

sealed class PropertiesViewState(val type: Type) {

    enum class Type {
        PROPERTIES,
        LOADING,
        EMPTY_STATE,
    }

    data class Properties(
        val id: Long,
        val propertyType: String,
        val featuredPicture: NativePhoto,
        val address: String,
        val price: String,
        val isSold: Boolean,
        val room: NativeText,
        val bathroom: NativeText,
        val bedroom: NativeText,
        val surface: String,
        val onClickEvent: EquatableCallback,
    ) : PropertiesViewState(Type.PROPERTIES)

    object LoadingState : PropertiesViewState(Type.LOADING)

    object EmptyState : PropertiesViewState(Type.EMPTY_STATE)
}