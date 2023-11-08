package com.emplk.realestatemanager.domain.filter

import android.util.Log
import com.emplk.realestatemanager.domain.property.PropertyRepository
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal
import java.time.LocalDateTime
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
        entryDateMin: LocalDateTime?,
        entryDateMax: LocalDateTime?,
        isSold: Boolean?,
        locationLatLong: LatLng?,
        radiusInMiles: Int,
    ): List<Long> =
        propertyRepository.getFilteredProperties(
            propertyType,
            minPrice,
            maxPrice,
            minSurface,
            maxSurface,
            entryDateMin,
            entryDateMax,
            isSold
        ).let { propertiesIds ->
            return filterPropertiesByDistanceUseCase.invoke(propertiesIds, locationLatLong, radiusInMiles)
        }
}