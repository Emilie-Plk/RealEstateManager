package com.emplk.realestatemanager.domain.autocomplete

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.transformLatest
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class GetCurrentPredictionAddressesFlowWithDebounceUseCase @Inject constructor(
    private val getAddressPredictionsUseCase: GetAddressPredictionsUseCase,
) {
    suspend fun invoke(
        input: MutableStateFlow<String?>,  // export those params in dedicated repository
        hasEditTextFocus: MutableStateFlow<Boolean>,
        isPredictionSelectedByUser: MutableStateFlow<Boolean?>
    ): Flow<PredictionWrapper?> = input.transformLatest { currentInput ->
        if (currentInput.isNullOrBlank() || currentInput.length < 3 || isPredictionSelectedByUser.value == true || hasEditTextFocus.value.not()) {
            emit(null)
        } else {
            delay(400.milliseconds)
            emit(getAddressPredictionsUseCase.invoke(currentInput))
        }
    }
}