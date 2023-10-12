package com.emplk.realestatemanager.domain.autocomplete

import com.emplk.realestatemanager.domain.property_draft.address.GetIsPredictionSelectedByUserUseCase
import com.emplk.realestatemanager.domain.property_draft.address.SelectedAddressStateRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class GetCurrentPredictionAddressesFlowWithDebounceUseCase @Inject constructor(
    private val selectedAddressStateRepository: SelectedAddressStateRepository,
    private val getAddressPredictionsUseCase: GetAddressPredictionsUseCase,
    private val getCurrentAddressInputUseCase: GetCurrentAddressInputUseCase,
    private val getIsPredictionSelectedByUserUseCase: GetIsPredictionSelectedByUserUseCase,
) {

    suspend fun invoke(): Flow<PredictionWrapper?> =
        selectedAddressStateRepository.getCurrentAddressInput().transformLatest { currentInput ->
            if (currentInput.isNullOrBlank() ||
                currentInput.length < 3 ||
                getIsPredictionSelectedByUserUseCase.invoke().value == true ||
                selectedAddressStateRepository.getHasAddressFocus().value == null ||
                selectedAddressStateRepository.getHasAddressFocus().value == false
            ) {
                emit(null)
            } else {
                delay(400.milliseconds)
                emit(getAddressPredictionsUseCase.invoke(currentInput))
            }
        }
}