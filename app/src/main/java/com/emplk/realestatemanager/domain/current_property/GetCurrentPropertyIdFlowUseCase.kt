package com.emplk.realestatemanager.domain.current_property

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrentPropertyIdFlowUseCase @Inject constructor(
    private val currentPropertyRepository: CurrentPropertyRepository,
) {
    fun invoke(): Flow<Long?> = currentPropertyRepository.getCurrentPropertyIdAsFlow()
}