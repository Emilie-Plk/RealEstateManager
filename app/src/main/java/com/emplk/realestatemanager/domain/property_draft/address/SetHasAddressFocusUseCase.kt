package com.emplk.realestatemanager.domain.property_draft.address

import javax.inject.Inject

class SetHasAddressFocusUseCase @Inject constructor(
    private val predictionAddressStateRepository: PredictionAddressStateRepository,
) {
    fun invoke(hasFocus: Boolean) { predictionAddressStateRepository.setHasAddressFocus(hasFocus) }
}