package com.emplk.realestatemanager.domain.property_draft.address

import javax.inject.Inject

class SetCurrentAddressInputUseCase @Inject constructor(
    private val selectedAddressStateRepository: SelectedAddressStateRepository,
) {
    fun invoke(address: String?) {
        selectedAddressStateRepository.setCurrentAddressInput(address)
    }
}