package com.emplk.realestatemanager.domain.property_draft.address

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow

interface SelectedAddressStateRepository {
    fun setCurrentAddressInput(address: String?)
    fun getCurrentAddressInput(): Flow<String?>
    fun setIsPredictionSelectedByUser(isSelected: Boolean)
    fun getIsPredictionSelectedByUser(): MutableStateFlow<Boolean?>
    fun setHasAddressFocus(hasFocus: Boolean)
    fun getHasAddressFocus(): MutableStateFlow<Boolean?>
    fun resetSelectedAddressState()
}
