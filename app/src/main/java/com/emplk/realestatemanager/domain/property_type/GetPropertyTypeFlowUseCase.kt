package com.emplk.realestatemanager.domain.property_type

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPropertyTypeFlowUseCase @Inject constructor(
    private val propertyTypeRepository: PropertyTypeRepository
) {
    fun invoke(): Flow<Map<Long, String>> = propertyTypeRepository.getPropertyTypeListFlow()
}