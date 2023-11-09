package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.property.PropertyRepository
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

class GetFilteredPropertiesUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
) {
    fun invoke(
        propertyType: String?,
        minPrice: BigDecimal,
        maxPrice: BigDecimal,
        minSurface: BigDecimal,
        maxSurface: BigDecimal,
        amenities: List<AmenityType>,
        entryDateMin: LocalDateTime?,
        entryDateMax: LocalDateTime?,
        isSold: Boolean?,
    ): Flow<Int> {
        return propertyRepository.getFilteredPropertiesCount(
            propertyType,
            minPrice,
            maxPrice,
            minSurface,
            maxSurface,
            amenities.contains(AmenityType.SCHOOL),
            amenities.contains(AmenityType.PARK),
            amenities.contains(AmenityType.SHOPPING_MALL),
            amenities.contains(AmenityType.RESTAURANT),
            amenities.contains(AmenityType.CONCIERGE),
            amenities.contains(AmenityType.GYM),
            amenities.contains(AmenityType.PUBLIC_TRANSPORTATION),
            amenities.contains(AmenityType.HOSPITAL),
            amenities.contains(AmenityType.LIBRARY),
            entryDateMin,
            entryDateMax,
            isSold,
        )
    }

}