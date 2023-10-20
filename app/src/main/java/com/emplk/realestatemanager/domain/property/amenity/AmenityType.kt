package com.emplk.realestatemanager.domain.property.amenity

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.emplk.realestatemanager.R

enum class AmenityType(
    val id: Long,
    @StringRes val stringRes: Int,
    @DrawableRes val iconDrawable: Int
) {
    SCHOOL(1, R.string.amenity_school, R.drawable.baseline_school_24),
    PARK(2, R.string.amenity_park, R.drawable.baseline_park_24),
    SHOPPING_MALL(3, R.string.amenity_shopping_mall, R.drawable.baseline_shopping_cart_24),
    RESTAURANT(4, R.string.amenity_restaurant, R.drawable.baseline_restaurant_24),
    CONCIERGE(5, R.string.amenity_concierge_service, R.drawable.baseline_person_24),
    GYM(6, R.string.amenity_gym, R.drawable.baseline_dumbbell_24),
    PUBLIC_TRANSPORTATION(7, R.string.amenity_public_transportation, R.drawable.baseline_directions_bus_24),
    HOSPITAL(8, R.string.amenity_hospital, R.drawable.baseline_local_hospital_24),
    LIBRARY(9, R.string.amenity_library, R.drawable.baseline_local_library_24),
}

enum class Animal {
    CAT,
    DOG,
    BIRD,
    FISH,
    OTHER
}