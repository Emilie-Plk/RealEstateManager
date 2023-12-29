package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.filter.model.SearchEntity
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
        return propertyRepository.getFilteredPropertiesCountRawQuery(
            SearchEntity(
                propertyType = if (propertyType == "All") null else propertyType,
                minPrice = minPrice,
                maxPrice = maxPrice,
                minSurface = minSurface,
                maxSurface = maxSurface,
                amenitySchool = amenities.contains(AmenityType.SCHOOL),
                amenityPark = amenities.contains(AmenityType.PARK),
                amenityShopping = amenities.contains(AmenityType.SHOPPING_MALL),
                amenityRestaurant = amenities.contains(AmenityType.RESTAURANT),
                amenityConcierge = amenities.contains(AmenityType.CONCIERGE),
                amenityGym = amenities.contains(AmenityType.GYM),
                amenityTransport = amenities.contains(AmenityType.PUBLIC_TRANSPORTATION),
                amenityHospital = amenities.contains(AmenityType.HOSPITAL),
                amenityLibrary = amenities.contains(AmenityType.LIBRARY),
                entryDateEpochMin = entryDateMin,
                entryDateEpochMax = now,
                isSold = when (propertySaleState) {
                    PropertySaleState.SOLD -> true
                    PropertySaleState.FOR_SALE -> false
                    PropertySaleState.ALL -> null
                    else -> null
                },
            )
        )
    }
}