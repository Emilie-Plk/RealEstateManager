package com.emplk.realestatemanager.ui.add.amenity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.emplk.realestatemanager.ui.utils.EquatableCallback

data class AmenityViewStateItem(
    val id: Long,
    val name: String,
    var isChecked: Boolean? = true,
    val onCheckBoxClicked: EquatableCallback,
    @DrawableRes val iconDrawable: Int,
    @StringRes val stringRes: Int,
)