package com.emplk.realestatemanager.domain.current_property

import javax.inject.Inject

class GetCurrentPropertyIdFlowUseCase @Inject constructor(
    private val currentPropertyRepository: CurrentPropertyRepository,
) {
    fun invoke() = currentPropertyRepository.getCurrentPropertyIdAsFlow()
}