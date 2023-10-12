package com.emplk.realestatemanager.domain.property_draft.address

import javax.inject.Inject

class ResetSelectedAddressStateUseCase @Inject constructor(
    private val selectedAddressStateRepository: SelectedAddressStateRepository,
) {
    fun invoke() {
        selectedAddressStateRepository.resetSelectedAddressState()
    }
}