package com.emplk.realestatemanager.domain.property_draft

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class IsPropertyFormInProgressUseCase @Inject constructor(private val propertyFormRepository: PropertyFormRepository) {

    fun invoke(): Flow<Boolean> =
        propertyFormRepository.isPropertyFormInProgressAsFlow()
}