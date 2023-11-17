package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.property.PropertyRepository
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.ui.filter.PropertySaleState
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

class GetFilteredPropertiesCountAsFlowUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val clock: Clock,
) {
    fun invoke(
        propertyType: String?,
        minPrice: BigDecimal,
        maxPrice: BigDecimal,
        minSurface: BigDecimal,
        maxSurface: BigDecimal,
        amenities: List<AmenityType>,
        entryDateMin: Long?,
        propertySaleState: PropertySaleState?,
    ): Flow<Int> {
        val now = LocalDateTime.now(clock).atZone(ZoneId.systemDefault()).toInstant().toEpochMilli()
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
            now,
            when (propertySaleState) {
                PropertySaleState.SOLD -> true
                PropertySaleState.FOR_SALE -> false
                PropertySaleState.ALL -> null
                else -> null
            },
        )
    }
}