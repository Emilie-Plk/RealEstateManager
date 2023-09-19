package com.emplk.realestatemanager.domain.amenity

import androidx.annotation.StringRes
import com.emplk.realestatemanager.R

enum class AmenityType(val id: Long, @StringRes val type: Int) {
    SCHOOL(1, R.string.amenity_school),
    PARK(2, R.string.amenity_park),
    SHOPPING_MALL(3, R.string.amenity_shopping_mall),
    RESTAURANT(4, R.string.amenity_restaurant),
    CONCIERGE(5, R.string.amenity_concierge_service),
    GYM(6, R.string.amenity_gym),
    PUBLIC_TRANSPORTATION(7, R.string.amenity_public_transportation),
    HOSPITAL(8, R.string.amenity_hospital),
    LIBRARY(9, R.string.amenity_library),
}