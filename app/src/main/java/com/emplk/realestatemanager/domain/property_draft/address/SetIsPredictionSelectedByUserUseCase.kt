package com.emplk.realestatemanager.domain.property_draft.address

import javax.inject.Inject

class SetIsPredictionSelectedByUserUseCase @Inject constructor(
    private val selectedAddressStateRepository: SelectedAddressStateRepository,
) {
    fun invoke(isSelected: Boolean) {
        selectedAddressStateRepository.setIsPredictionSelectedByUser(isSelected)
    }
}