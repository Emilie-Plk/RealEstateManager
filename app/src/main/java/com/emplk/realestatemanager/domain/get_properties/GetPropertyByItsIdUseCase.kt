package com.emplk.realestatemanager.domain.get_properties

import com.emplk.realestatemanager.domain.property.PropertiesWithPicturesAndLocationEntity
import com.emplk.realestatemanager.domain.property.PropertyRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPropertyByItsIdUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository
) {
    suspend fun invoke(id: Long): Flow<PropertiesWithPicturesAndLocationEntity> =
        propertyRepository.getPropertyByIdAsFlow(id)
}