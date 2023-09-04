package com.emplk.realestatemanager.domain.amenity

import androidx.annotation.StringRes
import com.emplk.realestatemanager.R

enum class AmenityType(@StringRes name: Int) {
    SCHOOL(R.string.amenity_school),
    PARK(R.string.amenity_park),
    SHOPPING_MALL(R.string.amenity_shopping_mall),
    RESTAURANT(R.string.amenity_restaurant),
    CONCIERGE(R.string.amenity_concierge_service),
    GYM(R.string.amenity_gym),
    PUBLIC_TRANSPORTATION(R.string.amenity_public_transportation),
    HOSPITAL(R.string.amenity_hospital),
    LIBRARY(R.string.amenity_library),
}