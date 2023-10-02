package com.emplk.realestatemanager.domain.property.type_price_surface

import com.emplk.realestatemanager.domain.property.PropertyRepository
import javax.inject.Inject

class GetPropertyPriceTypeAndSurfaceByIdUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository,
)   {
    suspend fun invoke(propertyId: Long): PropertyTypePriceAndSurfaceEntity {
        return propertyRepository.getPropertyTypeSurfaceAndPriceById(propertyId)
    }
}