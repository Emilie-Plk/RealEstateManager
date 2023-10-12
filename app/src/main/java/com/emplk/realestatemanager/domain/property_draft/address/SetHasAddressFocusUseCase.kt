package com.emplk.realestatemanager.domain.property_draft.address

import javax.inject.Inject

class SetHasAddressFocusUseCase @Inject constructor(
    private val selectedAddressStateRepository: SelectedAddressStateRepository,
) {
    fun invoke(hasFocus: Boolean) {
        selectedAddressStateRepository.setHasAddressFocus(hasFocus)
    }
}