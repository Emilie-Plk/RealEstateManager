package com.emplk.realestatemanager.ui.property_list

import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto

sealed class PropertyViewState(val type: Type) {

    enum class Type {
        PROPERTY,
        EMPTY_STATE,
    }

    object EmptyState : PropertyViewState(Type.EMPTY_STATE)

    data class Property(
        val id: Long,
        val typeOfProperty: String,
        val featuredPicture: NativePhoto,
        val address: String,
        val price: String,
        val isSold: Boolean,
        val onClickEvent: EquatableCallback,
    ) : PropertyViewState(Type.PROPERTY)
}