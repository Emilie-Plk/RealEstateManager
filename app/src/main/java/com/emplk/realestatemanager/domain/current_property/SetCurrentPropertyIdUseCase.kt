package com.emplk.realestatemanager.domain.current_property

import javax.inject.Inject

class SetCurrentPropertyIdUseCase @Inject constructor(
    private val currentPropertyRepository: CurrentPropertyRepository,
) {
    fun invoke(id: Long) { currentPropertyRepository.setCurrentPropertyId(id) }
}