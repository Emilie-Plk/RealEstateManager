package com.emplk.realestatemanager.domain.filter

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetPropertiesFilterFlowUseCase @Inject constructor(
    private val propertiesFilterRepository: PropertiesFilterRepository
) {
    fun invoke(): Flow<PropertiesFilterEntity?> = propertiesFilterRepository.getPropertiesFilter()
}