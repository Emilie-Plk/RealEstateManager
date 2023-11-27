package com.emplk.realestatemanager.domain.property_draft.address

import com.emplk.realestatemanager.data.property_draft.address.PredictionAddressState
import kotlinx.coroutines.flow.Flow

interface PredictionAddressStateRepository {
    fun setCurrentAddressInput(userInput: String?)
    fun setIsPredictionSelectedByUser(isSelected: Boolean)
    fun setHasAddressFocus(hasFocus: Boolean)
    fun getPredictionAddressStateAsFlow(): Flow<PredictionAddressState>
    fun resetSelectedAddressState()
}
