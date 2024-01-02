package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.filter.model.PropertiesFilterEntity
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

    private fun doesMatchPriceFilter(price: BigDecimal, filter: PropertiesFilterEntity): Boolean {
        val (minPrice, maxPrice) = filter.minMaxPrice
        return (minPrice == BigDecimal.ZERO && maxPrice == BigDecimal.ZERO) ||
                (price in minPrice..maxPrice) ||
                (minPrice == BigDecimal.ZERO && price <= maxPrice) ||
                (maxPrice == BigDecimal.ZERO && price >= minPrice)
    }

    private fun doesMatchSurfaceFilter(surface: BigDecimal, filter: PropertiesFilterEntity): Boolean {
        val (minSurface, maxSurface) = filter.minMaxSurface
        return (minSurface == BigDecimal.ZERO && maxSurface == BigDecimal.ZERO) ||
                (surface in minSurface..maxSurface) ||
                (minSurface == BigDecimal.ZERO && surface <= maxSurface) ||
                (maxSurface == BigDecimal.ZERO && surface >= minSurface)
    }

    private fun doesMatchEntryDateFilter(entryDate: LocalDateTime, filter: PropertiesFilterEntity): Boolean {
        val entryDateMin = filter.entryDate.searchedEntryDateLambda.invoke(clock)
        return entryDateMin == null || entryDate.isAfter(entryDateMin)
    }

    private fun doesMatchSaleStateFilter(isSold: Boolean, filter: PropertiesFilterEntity): Boolean =
        filter.availableForSale == PropertySaleState.ALL || filter.availableForSale == PropertySaleState.FOR_SALE && !isSold || filter.availableForSale == PropertySaleState.SOLD && isSold

    private fun doesMatchAmenitiesFilter(amenities: List<AmenityType>, filter: PropertiesFilterEntity): Boolean =
        filter.amenities.isEmpty() || amenities.containsAll(filter.amenities)
}