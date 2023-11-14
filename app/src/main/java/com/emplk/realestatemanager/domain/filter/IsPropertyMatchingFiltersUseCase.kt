package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.ui.filter.EntryDateState
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
        propertiesFilter: PropertiesFilterEntity
    ): Boolean = doesMatchPropertyTypeFilter(propertyType, propertiesFilter) &&
            doesMatchPriceFilter(price, propertiesFilter) &&
            doesMatchSurfaceFilter(surface, propertiesFilter) &&
            doesMatchEntryDateFilter(entryDate, propertiesFilter) &&
            doesMatchSaleStateFilter(isSold, propertiesFilter) &&
            doesMatchAmenitiesFilter(amenities, propertiesFilter) &&
            doesMatchEntryDateFilter(entryDate, propertiesFilter)


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
        if (filter.entryDate == EntryDateState.ALL) return true
        val now = LocalDateTime.now(clock)
        val entryDateMin = when (filter.entryDate) {
            EntryDateState.LESS_THAN_1_YEAR -> now.minusYears(1)
            EntryDateState.LESS_THAN_6_MONTHS -> now.minusMonths(6)
            EntryDateState.LESS_THAN_3_MONTHS -> now.minusMonths(3)
            EntryDateState.LESS_THAN_1_MONTH -> now.minusMonths(1)
            EntryDateState.LESS_THAN_1_WEEK -> now.minusWeeks(1)
            else -> null
        }
        return entryDateMin == null || entryDate.isAfter(entryDateMin)
    }

    private fun doesMatchSaleStateFilter(isSold: Boolean, filter: PropertiesFilterEntity): Boolean =
        filter.availableForSale == PropertySaleState.ALL || filter.availableForSale == PropertySaleState.FOR_SALE && !isSold || filter.availableForSale == PropertySaleState.SOLD && isSold

    private fun doesMatchAmenitiesFilter(amenities: List<AmenityType>, filter: PropertiesFilterEntity): Boolean =
        filter.amenities.isEmpty() || amenities.containsAll(filter.amenities)
}