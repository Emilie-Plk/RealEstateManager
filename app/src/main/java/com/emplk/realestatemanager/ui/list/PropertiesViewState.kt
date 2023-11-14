package com.emplk.realestatemanager.ui.list

import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import java.math.BigDecimal
import java.time.LocalDateTime

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
        val humanReadablePrice: String,
        val price: BigDecimal,
        val isSold: Boolean,
        val room: NativeText,
        val bathroom: NativeText,
        val bedroom: NativeText,
        val humanReadableSurface: String,
        val surface: BigDecimal,
        val entryDate: LocalDateTime,
        val amenities: List<AmenityType>,
        val onClickEvent: EquatableCallback,
    ) : PropertiesViewState(Type.PROPERTIES)

    object LoadingState : PropertiesViewState(Type.LOADING)

    data class EmptyState(val onAddClick: EquatableCallback) : PropertiesViewState(Type.EMPTY_STATE)
}