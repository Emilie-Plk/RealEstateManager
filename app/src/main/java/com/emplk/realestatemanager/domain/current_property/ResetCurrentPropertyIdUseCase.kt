package com.emplk.realestatemanager.domain.current_property

import javax.inject.Inject

class ResetCurrentPropertyIdUseCase @Inject constructor(
    private val currentPropertyRepository: CurrentPropertyRepository,
) {
    fun invoke() {  // TODO : revoir et ptet juste m'en servir aussi nav back
        currentPropertyRepository.resetCurrentPropertyId()
    }
}