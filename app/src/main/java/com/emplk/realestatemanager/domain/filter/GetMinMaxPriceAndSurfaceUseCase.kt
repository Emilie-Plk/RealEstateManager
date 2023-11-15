package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.property.PropertyRepository
import javax.inject.Inject

class GetMinMaxPriceAndSurfaceUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val convertToSquareFeetDependingOnLocaleUseCase: ConvertToSquareFeetDependingOnLocaleUseCase,
    private val convertPriceDependingOnLocaleUseCase: ConvertPriceDependingOnLocaleUseCase,
) {

    suspend fun invoke(): PropertyMinMaxStatsEntity = propertyRepository.getMinMaxPricesAndSurfaces()
        .let { propertyMinMaxStatsEntity ->
            PropertyMinMaxStatsEntity(
                minPrice = convertPriceDependingOnLocaleUseCase.invoke(propertyMinMaxStatsEntity.minPrice),
                maxPrice = convertPriceDependingOnLocaleUseCase.invoke(propertyMinMaxStatsEntity.maxPrice),
                minSurface = convertToSquareFeetDependingOnLocaleUseCase.invoke(propertyMinMaxStatsEntity.minSurface),
                maxSurface = convertToSquareFeetDependingOnLocaleUseCase.invoke(propertyMinMaxStatsEntity.maxSurface),
            )
        }
}