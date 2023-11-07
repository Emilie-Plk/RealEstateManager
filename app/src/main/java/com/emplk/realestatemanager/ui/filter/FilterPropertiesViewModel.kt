package com.emplk.realestatemanager.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.domain.autocomplete.GetCurrentPredictionAddressesFlowWithDebounceUseCase
import com.emplk.realestatemanager.domain.autocomplete.PredictionWrapper
import com.emplk.realestatemanager.domain.connectivity.IsInternetEnabledFlowUseCase
import com.emplk.realestatemanager.domain.filter.FilteredPropertyIdsRepository
import com.emplk.realestatemanager.domain.filter.GetFilteredPropertiesUseCase
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionViewState
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class FilterPropertiesViewModel @Inject constructor(
    private val getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase,
    private val filteredPropertyIdsRepository: FilteredPropertyIdsRepository,
    private val getCurrentPredictionAddressesFlowWithDebounceUseCase: GetCurrentPredictionAddressesFlowWithDebounceUseCase,
    private val isInternetEnabledFlowUseCase: IsInternetEnabledFlowUseCase, // no internet = location search not available
) : ViewModel() {

    private val filterParamsMutableStateFlow = MutableStateFlow(FilterParams())

    val viewState: LiveData<FilterViewState> = liveData {
        combine(
            filterParamsMutableStateFlow,
            getCurrentPredictionAddressesFlowWithDebounceUseCase.invoke()
        ) { filterParams, predictionAddresses ->
            FilterViewState(
                type = filterParams.type,
                minPrice = filterParams.minPrice.toString(),
                maxPrice = filterParams.maxPrice.toString(),
                minSurface = filterParams.minSurface.toString(),
                maxSurface = filterParams.maxSurface.toString(),
                locationPredictions = mapPredictionsToViewState(predictionAddresses),
                location = filterParams.address,
                isRadiusEditTextVisible = filterParams.isAddressValid,
                entryDate = filterParams.entryDate,
                availableForSale = filterParams.isSold == false,
                filterButtonText = if (filterParams.isSold == true) {
                    NativeText.Simple("Filter available properties")
                } else {
                    NativeText.Simple("Filter available properties")
                },
                onCancelClicked = EquatableCallback { filterParamsMutableStateFlow.update { FilterParams() } },
                onFilterClicked = EquatableCallback { }
            )
        }
    }


    private fun mapPredictionsToViewState(currentPredictionAddresses: PredictionWrapper?): List<PredictionViewState> {
        return when (currentPredictionAddresses) {
            is PredictionWrapper.Success -> {
                currentPredictionAddresses.predictions.map { prediction ->
                    PredictionViewState.Prediction(
                        address = prediction,
                        onClickEvent = EquatableCallbackWithParam { selectedAddress ->
                            filterParamsMutableStateFlow.update {
                                it.copy(
                                    address = selectedAddress,
                                    isAddressValid = true
                                )
                            }
                            //   selectedAddressStateRepository.setIsPredictionSelectedByUser(true)
                        }
                    )
                }
            }

            is PredictionWrapper.NoResult -> listOf(PredictionViewState.EmptyState)
            is PredictionWrapper.Error -> emptyList<PredictionViewState>().also { println("Error: ${currentPredictionAddresses.error}") }
            is PredictionWrapper.Failure -> emptyList<PredictionViewState>().also {
                println("Failure: ${currentPredictionAddresses.failure}")
            }

            else -> emptyList()
        }
    }
}

data class FilterParams(
    val type: String? = null,
    val minPrice: BigDecimal = BigDecimal.ZERO,
    val maxPrice: BigDecimal = BigDecimal.ZERO,
    val minSurface: BigDecimal = BigDecimal.ZERO,
    val maxSurface: BigDecimal = BigDecimal.ZERO,
    val address: String? = null,
    val isAddressValid: Boolean = false,
    val radius: BigDecimal = BigDecimal.ZERO,
    val isSold: Boolean? = false,
    val entryDate: EntryDateStatus = EntryDateStatus.NONE,
)