package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.property.PropertyRepository
import javax.inject.Inject

class GetMinMaxPriceAndSurfaceConvertedUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val convertToSquareFeetDependingOnLocaleUseCase: ConvertToSquareFeetDependingOnLocaleUseCase,
    private val convertPriceDependingOnLocaleUseCase: ConvertPriceDependingOnLocaleUseCase,
) {

    suspend fun invoke(): PropertyMinMaxStatsEntity {
        val propertyMinMaxStatsEntity = propertyRepository.getMinMaxPricesAndSurfaces()

        return PropertyMinMaxStatsEntity(
            minPrice = convertPriceDependingOnLocaleUseCase.invoke(propertyMinMaxStatsEntity.minPrice),
            maxPrice = convertPriceDependingOnLocaleUseCase.invoke(propertyMinMaxStatsEntity.maxPrice),
            minSurface = convertToSquareFeetDependingOnLocaleUseCase.invoke(propertyMinMaxStatsEntity.minSurface),
            maxSurface = convertToSquareFeetDependingOnLocaleUseCase.invoke(propertyMinMaxStatsEntity.maxSurface),
        )
    }
}