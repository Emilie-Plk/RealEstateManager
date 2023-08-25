package com.emplk.realestatemanager.domain.amenity

import androidx.annotation.DrawableRes

data class AmenityEntity(
    val name: String,
    @DrawableRes val iconDrawableRes: Int,
)