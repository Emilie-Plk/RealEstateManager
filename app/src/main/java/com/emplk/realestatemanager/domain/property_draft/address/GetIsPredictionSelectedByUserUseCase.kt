package com.emplk.realestatemanager.domain.property_draft.address

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class GetIsPredictionSelectedByUserUseCase @Inject constructor(
    private val selectedAddressStateRepository: SelectedAddressStateRepository,
) {
    fun invoke(): MutableStateFlow<Boolean?> = selectedAddressStateRepository.getIsPredictionSelectedByUser()
}