package com.emplk.realestatemanager.ui.add.amenity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam

data class AmenityViewStateItem(
    val id: Long,
    val name: String,
    val isChecked: Boolean,
    val onCheckBoxClicked: EquatableCallbackWithParam<Boolean>,
    @DrawableRes val iconDrawable: Int,
    @StringRes val stringRes: Int,
)