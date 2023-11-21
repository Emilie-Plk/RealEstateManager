package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.ui.filter.PropertySaleState
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDateTime
import javax.inject.Inject

class IsPropertyMatchingFiltersUseCase @Inject constructor(
    private val clock: Clock
) {
    fun invoke(
        propertyType: String?,
        price: BigDecimal,
        surface: BigDecimal,
        amenities: List<AmenityType>,
        entryDate: LocalDateTime,
        isSold: Boolean,
        propertiesFilter: PropertiesFilterEntity?
    ): Boolean = propertiesFilter == null ||
            doesMatchPropertyTypeFilter(propertyType, propertiesFilter) &&
            doesMatchPriceFilter(price, propertiesFilter) &&
            doesMatchSurfaceFilter(surface, propertiesFilter) &&
            doesMatchEntryDateFilter(entryDate, propertiesFilter) &&
            doesMatchSaleStateFilter(isSold, propertiesFilter) &&
            doesMatchAmenitiesFilter(amenities, propertiesFilter)


    private fun doesMatchPropertyTypeFilter(propertyType: String?, filter: PropertiesFilterEntity): Boolean =
        filter.propertyType == null || propertyType == filter.propertyType

    private fun doesMatchPriceFilter(price: BigDecimal, filter: PropertiesFilterEntity): Boolean =
        filter.minMaxPrice.first == BigDecimal.ZERO && filter.minMaxPrice.second == BigDecimal.ZERO ||
                price >= filter.minMaxPrice.first && price <= filter.minMaxPrice.second ||
                filter.minMaxPrice.first == BigDecimal.ZERO && price <= filter.minMaxPrice.second ||
                filter.minMaxPrice.second == BigDecimal.ZERO && price >= filter.minMaxPrice.first

    private fun doesMatchSurfaceFilter(surface: BigDecimal, filter: PropertiesFilterEntity): Boolean =
        filter.minMaxSurface.first == BigDecimal.ZERO && filter.minMaxSurface.second == BigDecimal.ZERO ||
                surface >= filter.minMaxSurface.first && surface <= filter.minMaxSurface.second ||
                filter.minMaxSurface.first == BigDecimal.ZERO && surface <= filter.minMaxSurface.second ||
                filter.minMaxSurface.second == BigDecimal.ZERO && surface >= filter.minMaxSurface.first

    private fun doesMatchEntryDateFilter(entryDate: LocalDateTime, filter: PropertiesFilterEntity): Boolean {
        val entryDateMin = filter.entryDate.searchedEntryDateLambda.invoke(clock)
        return entryDateMin == null || entryDate.isAfter(entryDateMin)
    }

    private fun doesMatchSaleStateFilter(isSold: Boolean, filter: PropertiesFilterEntity): Boolean =
        filter.availableForSale == PropertySaleState.ALL || filter.availableForSale == PropertySaleState.FOR_SALE && !isSold || filter.availableForSale == PropertySaleState.SOLD && isSold

    private fun doesMatchAmenitiesFilter(amenities: List<AmenityType>, filter: PropertiesFilterEntity): Boolean =
        filter.amenities.isEmpty() || amenities.containsAll(filter.amenities)
}