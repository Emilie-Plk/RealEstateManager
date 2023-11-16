package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.ui.filter.PropertySaleState
import java.math.BigDecimal
import javax.inject.Inject

class SetPropertiesFilterUseCase @Inject constructor(
    private val propertiesFilterRepository: PropertiesFilterRepository
) {
    fun invoke(
        propertyType: String?,
        minPrice: BigDecimal,
        maxPrice: BigDecimal,
        minSurface: BigDecimal,
        maxSurface: BigDecimal,
        selectedAmenities: List<AmenityType>,
        saleState: PropertySaleState,
        entryDateState: EntryDateState,
    ) = propertiesFilterRepository.setPropertiesFilter(
        PropertiesFilterEntity(
            propertyType = if (propertyType == "All") null else propertyType,
            minMaxPrice = Pair(minPrice, maxPrice),
            minMaxSurface = Pair(minSurface, maxSurface),
            entryDate = entryDateState,
            availableForSale = saleState,
            amenities = selectedAmenities,
        )
    )
}