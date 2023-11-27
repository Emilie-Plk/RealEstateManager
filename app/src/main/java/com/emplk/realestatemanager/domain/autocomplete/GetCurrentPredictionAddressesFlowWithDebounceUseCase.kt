package com.emplk.realestatemanager.domain.autocomplete

import com.emplk.realestatemanager.domain.property_draft.address.PredictionAddressStateRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

class GetCurrentPredictionAddressesFlowWithDebounceUseCase @Inject constructor(
    private val predictionAddressStateRepository: PredictionAddressStateRepository,
    private val getAddressPredictionsUseCase: GetAddressPredictionsUseCase,
) {

    fun invoke(): Flow<PredictionWrapper?> = predictionAddressStateRepository.getPredictionAddressStateAsFlow().mapLatest {
        if (it.currentInput == null ||
            it.currentInput.length < 3 ||
            it.isAddressPredictionSelectedByUser == true ||
            it.hasAddressPredictionFocus == false ||
            it.hasAddressPredictionFocus == null
        ) {
            null
        } else {
            delay(400.milliseconds)
            getAddressPredictionsUseCase.invoke(it.currentInput)
        }
    }.distinctUntilChanged()
}