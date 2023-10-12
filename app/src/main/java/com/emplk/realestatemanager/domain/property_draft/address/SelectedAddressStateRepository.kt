package com.emplk.realestatemanager.domain.property_draft.address

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface SelectedAddressStateRepository {
    fun getCurrentAddressInput(): Flow<String?>
    fun setCurrentAddressInput(address: String?)
    fun getIsPredictionSelectedByUser(): MutableStateFlow<Boolean?>
    fun setIsPredictionSelectedByUser(isSelected: Boolean)
    fun getHasAddressFocus(): MutableStateFlow<Boolean?>
    fun setHasAddressFocus(hasFocus: Boolean)
    fun resetSelectedAddressState()
}
