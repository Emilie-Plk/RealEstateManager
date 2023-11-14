package com.emplk.realestatemanager.domain.filter

import javax.inject.Inject

class ResetPropertiesFilterUseCase @Inject constructor(
    private val propertiesFilterRepository: PropertiesFilterRepository
) {
    fun invoke() = propertiesFilterRepository.resetPropertiesFilter()
}