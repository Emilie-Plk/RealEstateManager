package com.emplk.realestatemanager.ui.add.amenity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam

sealed class AmenityViewState(val type: Type) {
    enum class Type {
        AMENITY_CHECKBOX,
        AMENITY_ITEM,
    }


    data class AmenityCheckbox(
        val id: Long,
        val name: String,
        val isChecked: Boolean,
        val onCheckBoxClicked: EquatableCallbackWithParam<Boolean>,
        @DrawableRes val iconDrawable: Int,
        @StringRes val stringRes: Int,
    ) : AmenityViewState(Type.AMENITY_CHECKBOX)

    data class AmenityItem(
        @StringRes val stringRes: Int,
        @DrawableRes val iconDrawable: Int
    ) : AmenityViewState(Type.AMENITY_ITEM)
}