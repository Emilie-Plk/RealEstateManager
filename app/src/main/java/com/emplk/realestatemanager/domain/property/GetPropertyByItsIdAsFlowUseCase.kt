package com.emplk.realestatemanager.domain.property

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPropertyByItsIdAsFlowUseCase @Inject constructor(
    private val propertyRepository: PropertyRepository
) {
    fun invoke(propertyId: Long): Flow<PropertyEntity> =
        propertyRepository.getPropertyByIdAsFlow(propertyId)
}