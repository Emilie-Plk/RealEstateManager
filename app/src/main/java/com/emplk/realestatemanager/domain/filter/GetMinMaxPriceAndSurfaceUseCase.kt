package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.property.PropertyRepository
import javax.inject.Inject

class GetMinMaxPriceAndSurfaceUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
    private val convertSurfaceDependingOnLocaleUseCase: ConvertSurfaceDependingOnLocaleUseCase,
    private val convertPriceDependingOnLocaleUseCase: ConvertPriceDependingOnLocaleUseCase,
) {

    suspend fun invoke(): PropertyMinMaxStatsEntity = propertyRepository.getMinMaxPricesAndSurfaces()
        .let { propertyMinMaxStatsEntity ->
            PropertyMinMaxStatsEntity(
                minPrice = convertPriceDependingOnLocaleUseCase.invoke(propertyMinMaxStatsEntity.minPrice),
                maxPrice = convertPriceDependingOnLocaleUseCase.invoke(propertyMinMaxStatsEntity.maxPrice),
                minSurface = convertSurfaceDependingOnLocaleUseCase.invoke(propertyMinMaxStatsEntity.minSurface),
                maxSurface = convertSurfaceDependingOnLocaleUseCase.invoke(propertyMinMaxStatsEntity.maxSurface),
            )
        }
}