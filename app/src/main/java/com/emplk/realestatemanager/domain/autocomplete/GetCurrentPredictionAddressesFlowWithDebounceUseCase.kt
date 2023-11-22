package com.emplk.realestatemanager.domain.autocomplete

import com.emplk.realestatemanager.domain.property_draft.address.GetIsPredictionSelectedByUserUseCase
import com.emplk.realestatemanager.domain.property_draft.address.SelectedAddressStateRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combineTransform
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class GetCurrentPredictionAddressesFlowWithDebounceUseCase @Inject constructor(
    private val selectedAddressStateRepository: SelectedAddressStateRepository,
    private val getAddressPredictionsUseCase: GetAddressPredictionsUseCase,
    private val getIsPredictionSelectedByUserUseCase: GetIsPredictionSelectedByUserUseCase, // maybe a flag ?
) {

    fun invoke(): Flow<PredictionWrapper?> {
        val currentInputFlow = selectedAddressStateRepository.getCurrentAddressInput()
        val hasAddressFocusFlow = selectedAddressStateRepository.getHasAddressFocus()
        val isPredictionSelectedByUserFlow = getIsPredictionSelectedByUserUseCase.invoke()

        return combineTransform(
            currentInputFlow,
            hasAddressFocusFlow,
            isPredictionSelectedByUserFlow
        ) { currentInput, hasFocus, isPredictionSelected ->
            if (currentInput.isNullOrBlank() ||
                currentInput.length < 3 ||
                isPredictionSelected == true ||
                hasFocus == null ||
                hasFocus == false
            ) {
                emit(null)
            } else {
                delay(400.milliseconds)
                emit(getAddressPredictionsUseCase.invoke(currentInput))
            }
        }
    }
}