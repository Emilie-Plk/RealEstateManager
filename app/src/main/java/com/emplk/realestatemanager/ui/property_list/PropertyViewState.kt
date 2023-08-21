package com.emplk.realestatemanager.ui.property_list

import com.emplk.realestatemanager.ui.utils.EquatableCallback

sealed class PropertyViewStateItem(val type: Type) {

    enum class Type {
        PROPERTY,
        EMPTY_STATE,
    }

    object EmptyState : PropertyViewStateItem(Type.EMPTY_STATE)

    data class Property(
        val id: Long,
        val typeOfProperty: String,
        val featuredPicture: String,
        val location: String,
        val price: String,
        val onClickEvent: EquatableCallback,
    ) : PropertyViewStateItem(Type.PROPERTY)
}