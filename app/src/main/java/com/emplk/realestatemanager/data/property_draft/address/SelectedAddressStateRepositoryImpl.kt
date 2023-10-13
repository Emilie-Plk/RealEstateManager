package com.emplk.realestatemanager.data.property_draft.address

import com.emplk.realestatemanager.domain.property_draft.address.SelectedAddressStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class SelectedAddressStateRepositoryImpl @Inject constructor(
) : SelectedAddressStateRepository {
    private val currentAddressInputMutableStateFlow: MutableStateFlow<String?> = MutableStateFlow(null)
    private val isPredictionSelectedByUserMutableStateFlow = MutableStateFlow<Boolean?>(null)
    private val hasAddressFocusMutableStateFlow = MutableStateFlow<Boolean?>(null)
    override fun getCurrentAddressInput(): Flow<String?> = currentAddressInputMutableStateFlow

    override fun setCurrentAddressInput(address: String?) {
        currentAddressInputMutableStateFlow.tryEmit(address)
    }

    override fun getIsPredictionSelectedByUser(): MutableStateFlow<Boolean?> =
        isPredictionSelectedByUserMutableStateFlow

    override fun setIsPredictionSelectedByUser(isSelected: Boolean) {
        isPredictionSelectedByUserMutableStateFlow.tryEmit(isSelected)
    }

    override fun getHasAddressFocus(): MutableStateFlow<Boolean?> = hasAddressFocusMutableStateFlow

    override fun setHasAddressFocus(hasFocus: Boolean) {
        hasAddressFocusMutableStateFlow.tryEmit(hasFocus)
    }

    override fun resetSelectedAddressState() {
        currentAddressInputMutableStateFlow.tryEmit(null)
        isPredictionSelectedByUserMutableStateFlow.tryEmit(null)
        hasAddressFocusMutableStateFlow.tryEmit(null)
    }
}