package com.emplk.realestatemanager.data.property_draft.address

import com.emplk.realestatemanager.domain.property_draft.address.PredictionAddressStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class PredictionAddressStateRepositoryImpl @Inject constructor(
) : PredictionAddressStateRepository {
    private val predictionAddressMutableStateFlow = MutableStateFlow(PredictionAddressState())

    override fun setCurrentAddressInput(userInput: String?) {
        predictionAddressMutableStateFlow.update {
            it.copy(currentInput = userInput)
        }
    }

    override fun setIsPredictionSelectedByUser(isSelected: Boolean) {
        predictionAddressMutableStateFlow.update {
            it.copy(isAddressPredictionSelectedByUser = isSelected)
        }
    }

    override fun setHasAddressFocus(hasFocus: Boolean) {
        predictionAddressMutableStateFlow.update {
            it.copy(hasAddressPredictionFocus = hasFocus)
        }
    }

    override fun getPredictionAddressStateAsFlow(): Flow<PredictionAddressState> =
        predictionAddressMutableStateFlow

    override fun resetSelectedAddressState() {
        predictionAddressMutableStateFlow.tryEmit(PredictionAddressState())
    }
}

data class PredictionAddressState(
    val currentInput: String? = null,
    val isAddressPredictionSelectedByUser: Boolean? = null,
    val hasAddressPredictionFocus: Boolean? = null,
)