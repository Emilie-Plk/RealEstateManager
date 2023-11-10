package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.property.PropertyRepository
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.ui.filter.PropertySaleState
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
        entryDateMin: Long?,
        entryDateMax: Long?,
        propertySaleState: PropertySaleState,
    ): Flow<Int> {
        return propertyRepository.getFilteredPropertiesCount(
            if (propertyType == "All") null else propertyType,
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
            when (propertySaleState) {
                PropertySaleState.SOLD -> true
                PropertySaleState.FOR_SALE -> false
                PropertySaleState.ALL -> null
            },
        )
    }
}