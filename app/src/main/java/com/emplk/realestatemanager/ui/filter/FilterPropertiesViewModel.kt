package com.emplk.realestatemanager.ui.filter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.domain.autocomplete.GetCurrentPredictionAddressesFlowWithDebounceUseCase
import com.emplk.realestatemanager.domain.autocomplete.PredictionWrapper
import com.emplk.realestatemanager.domain.connectivity.IsInternetEnabledFlowUseCase
import com.emplk.realestatemanager.domain.filter.GetEntryDateByEntryDateStatusUseCase
import com.emplk.realestatemanager.domain.filter.GetFilteredPropertiesUseCase
import com.emplk.realestatemanager.domain.filter.GetMinMaxPriceAndSurfaceUseCase
import com.emplk.realestatemanager.domain.property_draft.address.SetHasAddressFocusUseCase
import com.emplk.realestatemanager.domain.property_draft.address.SetIsPredictionSelectedByUserUseCase
import com.emplk.realestatemanager.domain.property_draft.address.SetSelectedAddressStateUseCase
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionViewState
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class FilterPropertiesViewModel @Inject constructor(
    private val getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase,
    private val getCurrentPredictionAddressesFlowWithDebounceUseCase: GetCurrentPredictionAddressesFlowWithDebounceUseCase,
    private val setSelectedAddressStateUseCase: SetSelectedAddressStateUseCase,
    private val setHasAddressFocusUseCase: SetHasAddressFocusUseCase,
    private val setIsPredictionSelectedByUserUseCase: SetIsPredictionSelectedByUserUseCase,
    private val getEntryDateByEntryDateStatusUseCase: GetEntryDateByEntryDateStatusUseCase,
    private val getMinMaxPriceAndSurfaceUseCase: GetMinMaxPriceAndSurfaceUseCase,
    private val isInternetEnabledFlowUseCase: IsInternetEnabledFlowUseCase, // no internet = location search not available
) : ViewModel() {

    private val filterParamsMutableStateFlow = MutableStateFlow(FilterParams())

    private val filteredPropertyIdsMutableStateFlow: MutableStateFlow<List<Long>> =
        MutableStateFlow(emptyList())

    val viewState: LiveData<FilterViewState> = liveData {
        coroutineScope {
            launch {
                combine(
                    filterParamsMutableStateFlow,
                    getCurrentPredictionAddressesFlowWithDebounceUseCase.invoke(),
                    filteredPropertyIdsMutableStateFlow,
                ) { filterParams, predictionAddresses, filteredIds ->
                    val minMaxPriceAndSurface = getMinMaxPriceAndSurfaceUseCase.invoke()

                    filteredPropertyIdsMutableStateFlow.tryEmit(
                        getFilteredPropertiesUseCase.invoke(
                            filterParamsMutableStateFlow.value.type,
                            minPrice = filterParamsMutableStateFlow.value.minPrice,
                            maxPrice = filterParamsMutableStateFlow.value.maxPrice,
                            minSurface = filterParamsMutableStateFlow.value.minSurface,
                            maxSurface = filterParamsMutableStateFlow.value.maxSurface,
                            entryDateMin = getEntryDateByEntryDateStatusUseCase.invoke(filterParamsMutableStateFlow.value.entryDateStatus)?.first,
                            entryDateMax = getEntryDateByEntryDateStatusUseCase.invoke(filterParamsMutableStateFlow.value.entryDateStatus)?.second,
                            isSold = filterParamsMutableStateFlow.value.isSold,
                            locationLatLong = null,
                            radiusInMiles = 2000,
                        )
                    )

                    Log.d("COUCOU", "filteredPropertyIdsMutableStateFlow: ${filteredPropertyIdsMutableStateFlow.value} - ${getEntryDateByEntryDateStatusUseCase.invoke(filterParamsMutableStateFlow.value.entryDateStatus)?.first} - ${getEntryDateByEntryDateStatusUseCase.invoke(filterParamsMutableStateFlow.value.entryDateStatus)?.second}")


                    FilterViewState(
                        type = filterParams.type,
                        minPrice = minMaxPriceAndSurface.minPrice.setScale(0, RoundingMode.HALF_UP).intValueExact(),
                        maxPrice = minMaxPriceAndSurface.maxPrice.setScale(0, RoundingMode.HALF_UP).intValueExact(),
                        minSurface = minMaxPriceAndSurface.minSurface.setScale(0, RoundingMode.HALF_UP).intValueExact(),
                        maxSurface = minMaxPriceAndSurface.maxSurface.setScale(0, RoundingMode.HALF_UP).intValueExact(),
                        locationPredictions = mapPredictionsToViewState(predictionAddresses),
                        location = filterParams.location,
                        isRadiusEditTextVisible = filterParams.isLocationValid,
                        entryDate = filterParams.entryDateStatus,
                        availableForSale = filterParams.isSold == false,
                        filterButtonText = NativeText.Simple("Filter (${filteredIds.size})"),
                        onCancelClicked = EquatableCallback { filterParamsMutableStateFlow.update { FilterParams() } },
                        onFilterClicked = EquatableCallback {
                        }
                    )
                }.collectLatest { emit(it) }
            }
        }
    }

    private fun mapPredictionsToViewState(currentPredictionLocations: PredictionWrapper?): List<PredictionViewState> {
        return when (currentPredictionLocations) {
            is PredictionWrapper.Success -> {
                currentPredictionLocations.predictions.map { prediction ->
                    PredictionViewState.Prediction(
                        address = prediction,
                        onClickEvent = EquatableCallbackWithParam { selectedLocation ->
                            filterParamsMutableStateFlow.update {
                                it.copy(
                                    location = selectedLocation,
                                    isLocationValid = true,
                                )
                            }
                            setIsPredictionSelectedByUserUseCase.invoke(true)
                        }
                    )
                }
            }

            is PredictionWrapper.NoResult -> listOf(PredictionViewState.EmptyState)
            is PredictionWrapper.Error -> emptyList<PredictionViewState>().also { println("Error: ${currentPredictionLocations.error}") }
            is PredictionWrapper.Failure -> emptyList<PredictionViewState>().also {
                println("Failure: ${currentPredictionLocations.failure}")
            }

            else -> emptyList()
        }
    }

    fun onLocationChanged(input: String?) {
        if (filterParamsMutableStateFlow.value.isLocationValid && filterParamsMutableStateFlow.value.location != input) {
            setIsPredictionSelectedByUserUseCase.invoke(false)
            filterParamsMutableStateFlow.update {
                it.copy(
                    isLocationValid = false,
                    location = input
                )
            }
        } else {
            filterParamsMutableStateFlow.update {
                it.copy(
                    location = input
                )
            }
        }
        setSelectedAddressStateUseCase.invoke(input)
    }

    fun onAddressEditTextFocused(hasFocus: Boolean) {
        setHasAddressFocusUseCase.invoke(hasFocus)
    }

    fun onEntryDateStatusChanged(entryDateStatus: EntryDateStatus) {
        filterParamsMutableStateFlow.update {
            it.copy(
                entryDateStatus = entryDateStatus
            )
        }
    }
}

data class FilterParams(
    val type: String? = null,
    val minPrice: BigDecimal = BigDecimal.ZERO,
    val maxPrice: BigDecimal = BigDecimal.ZERO,
    val minSurface: BigDecimal = BigDecimal.ZERO,
    val maxSurface: BigDecimal = BigDecimal.ZERO,
    val location: String? = null,
    val isLocationValid: Boolean = false,
    val radius: BigDecimal = BigDecimal.ZERO,
    val isSold: Boolean? = false,
    val entryDateStatus: EntryDateStatus = EntryDateStatus.NONE,
)