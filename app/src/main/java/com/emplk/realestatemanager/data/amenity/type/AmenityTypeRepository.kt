package com.emplk.realestatemanager.data.amenity.type

import com.emplk.realestatemanager.domain.amenity.AmenityType
import com.emplk.realestatemanager.domain.amenity.type.AmenityTypeRepository
import javax.inject.Inject

class AmenityTypeRepositoryImpl @Inject constructor() : AmenityTypeRepository {
    private val amenityTypes = listOf(
        AmenityType.SCHOOL,
        AmenityType.PARK,
        AmenityType.SHOPPING_MALL,
        AmenityType.RESTAURANT,
        AmenityType.CONCIERGE,
        AmenityType.GYM,
        AmenityType.PUBLIC_TRANSPORTATION,
        AmenityType.HOSPITAL,
        AmenityType.LIBRARY,
    )

    override fun getAmenityTypes(): List<AmenityType> = amenityTypes
}