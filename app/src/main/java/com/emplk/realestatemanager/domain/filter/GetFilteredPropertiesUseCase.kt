package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.property.PropertyRepository
import com.google.android.gms.maps.model.LatLng
import java.math.BigDecimal
import javax.inject.Inject

class GetFilteredPropertiesUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val filterPropertiesByDistanceUseCase: FilterPropertiesByDistanceUseCase,
) {
    suspend fun invoke(
        propertyType: String?,
        minPrice: BigDecimal?,
        maxPrice: BigDecimal?,
        minSurface: BigDecimal?,
        maxSurface: BigDecimal?,
        entryDate: String?,
        isSold: Boolean?,
        locationLatLong: LatLng,
        radiusInMiles: Int,
    ): List<Long> =
        propertyRepository.getFilteredProperties(
            propertyType,
            minPrice,
            maxPrice,
            minSurface,
            maxSurface,
            entryDate,
            isSold
        ).let { propertiesIds ->
            return filterPropertiesByDistanceUseCase.invoke(propertiesIds, locationLatLong, radiusInMiles)
        }
}