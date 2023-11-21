package com.emplk.realestatemanager.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.filter.ConvertSearchedEntryDateRangeToEpochMilliUseCase
import com.emplk.realestatemanager.domain.filter.SearchedEntryDateRange
import com.emplk.realestatemanager.domain.filter.GetFilteredPropertiesCountAsFlowUseCase
import com.emplk.realestatemanager.domain.filter.GetMinMaxPriceAndSurfaceUseCase
import com.emplk.realestatemanager.domain.filter.GetPropertyTypeForFilterUseCase
import com.emplk.realestatemanager.domain.filter.SetPropertiesFilterUseCase
import com.emplk.realestatemanager.domain.locale_formatting.currency.FormatPriceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertSurfaceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertSurfaceToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToUsdDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.FormatAndRoundSurfaceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property.amenity.type.GetAmenityTypeUseCase
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.add.type.PropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class FilterPropertiesViewModel @Inject constructor(
    private val getFilteredPropertiesCountAsFlowUseCase: GetFilteredPropertiesCountAsFlowUseCase,
    private val convertSearchedEntryDateRangeToEpochMilliUseCase: ConvertSearchedEntryDateRangeToEpochMilliUseCase,
    private val getMinMaxPriceAndSurfaceUseCase: GetMinMaxPriceAndSurfaceUseCase,
    private val formatPriceToHumanReadableUseCase: FormatPriceToHumanReadableUseCase,
    private val convertSurfaceDependingOnLocaleUseCase: ConvertSurfaceDependingOnLocaleUseCase,
    private val formatAndRoundSurfaceToHumanReadableUseCase: FormatAndRoundSurfaceToHumanReadableUseCase,
    private val convertToUsdDependingOnLocaleUseCase: ConvertToUsdDependingOnLocaleUseCase,
    private val convertSurfaceToSquareFeetDependingOnLocaleUseCase: ConvertSurfaceToSquareFeetDependingOnLocaleUseCase,
    private val getPropertyTypeForFilterUseCase: GetPropertyTypeForFilterUseCase,
    private val getAmenityTypeUseCase: GetAmenityTypeUseCase,
    private val setPropertiesFilterUseCase: SetPropertiesFilterUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
) : ViewModel() {

    private val filterParamsMutableStateFlow = MutableStateFlow(FilterParams())
    private val onFilterClickMutableSharedFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val viewState: LiveData<FilterViewState> = liveData {
        filterParamsMutableStateFlow.flatMapLatest { filterParams ->
            getFilteredPropertiesCountAsFlowUseCase.invoke(
                propertyType = filterParams.propertyType,
                minPrice = convertToUsdDependingOnLocaleUseCase.invoke(filterParams.minPrice),
                maxPrice = convertToUsdDependingOnLocaleUseCase.invoke(filterParams.maxPrice),
                minSurface = convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(filterParams.minSurface),
                maxSurface = convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(filterParams.maxSurface),
                amenities = filterParams.selectedAmenities,
                entryDateMin = convertSearchedEntryDateRangeToEpochMilliUseCase.invoke(filterParams.searchedEntryDateRange),
                propertySaleState = filterParams.saleState,
            )
        }.collectLatest { filteredPropertiesCount ->
            val minMaxPriceAndSurface = getMinMaxPriceAndSurfaceUseCase.invoke()

            val propertyTypes = getPropertyTypeForFilterUseCase.invoke()
            val amenityTypes = getAmenityTypeUseCase.invoke()

            emit(FilterViewState(
                propertyType = filterParamsMutableStateFlow.value.propertyType,
                priceRange = NativeText.Arguments(
                    R.string.surface_or_price_range,
                    listOf(
                        formatPriceToHumanReadableUseCase.invoke(minMaxPriceAndSurface.minPrice),
                        formatPriceToHumanReadableUseCase.invoke(minMaxPriceAndSurface.maxPrice),
                    )
                ),
                minPrice = if (filterParamsMutableStateFlow.value.minPrice == BigDecimal.ZERO) ""
                else filterParamsMutableStateFlow.value.minPrice.setScale(
                    0,
                    RoundingMode.HALF_UP
                ).intValueExact().toString(),
                maxPrice = if (filterParamsMutableStateFlow.value.maxPrice == BigDecimal.ZERO) ""
                else filterParamsMutableStateFlow.value.maxPrice.setScale(
                    0,
                    RoundingMode.HALF_UP
                ).intValueExact().toString(),
                surfaceRange = NativeText.Arguments(
                    R.string.surface_or_price_range,
                    listOf(
                        formatAndRoundSurfaceToHumanReadableUseCase.invoke(
                            convertSurfaceDependingOnLocaleUseCase.invoke(
                                minMaxPriceAndSurface.minSurface
                            )
                        ),
                        formatAndRoundSurfaceToHumanReadableUseCase.invoke(
                            convertSurfaceDependingOnLocaleUseCase.invoke(
                                minMaxPriceAndSurface.maxSurface
                            )
                        ),
                    )
                ),
                minSurface = if (filterParamsMutableStateFlow.value.minSurface == BigDecimal.ZERO) ""
                else filterParamsMutableStateFlow.value.minSurface.setScale(
                    0,
                    RoundingMode.HALF_UP
                ).intValueExact().toString(),
                maxSurface = if (filterParamsMutableStateFlow.value.maxSurface == BigDecimal.ZERO) ""
                else filterParamsMutableStateFlow.value.maxSurface.setScale(
                    0,
                    RoundingMode.HALF_UP
                ).intValueExact().toString(),
                amenities = mapAmenityTypesToViewStates(amenityTypes),
                propertyTypes = propertyTypes.map { propertyType ->
                    PropertyTypeViewStateItem(
                        id = propertyType.key,
                        name = propertyType.value,
                    )
                },
                entryDate = filterParamsMutableStateFlow.value.searchedEntryDateRange,
                availableForSale = filterParamsMutableStateFlow.value.saleState,
                filterButtonText = when (filteredPropertiesCount) {
                    0 -> NativeText.Resource(R.string.filter_button_none)

                    1 -> NativeText.Resource(R.string.filter_button_one)

                    else -> NativeText.Arguments(
                        R.string.filter_button_nb_properties,
                        listOf(filteredPropertiesCount.toString())
                    )
                },
                isFilterButtonEnabled = filteredPropertiesCount != 0,
                onCancelClicked = EquatableCallback {
                    filterParamsMutableStateFlow.update { FilterParams() }
                    onFilterClickMutableSharedFlow.tryEmit(Unit)
                },
                onFilterClicked = EquatableCallback {
                    if (filterParamsMutableStateFlow.value != FilterParams()) {
                        viewModelScope.launch {
                            setPropertiesFilterUseCase.invoke(
                                filterParamsMutableStateFlow.value.propertyType,
                                convertToUsdDependingOnLocaleUseCase.invoke(filterParamsMutableStateFlow.value.minPrice),
                                convertToUsdDependingOnLocaleUseCase.invoke(filterParamsMutableStateFlow.value.maxPrice),
                                convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(
                                    filterParamsMutableStateFlow.value.minSurface
                                ),
                                convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(
                                    filterParamsMutableStateFlow.value.maxSurface
                                ),
                                filterParamsMutableStateFlow.value.selectedAmenities,
                                filterParamsMutableStateFlow.value.saleState,
                                filterParamsMutableStateFlow.value.searchedEntryDateRange,
                            )
                        }
                    }
                    setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                    onFilterClickMutableSharedFlow.tryEmit(Unit)
                }
            )
            )
        }
    }

    val viewEvent: LiveData<Event<Unit>> = liveData {
        onFilterClickMutableSharedFlow.collectLatest {
            emit(Event(Unit))
        }
    }

    private fun mapAmenityTypesToViewStates(amenityTypes: List<AmenityType>): List<AmenityViewState> =
        amenityTypes.map { amenityType ->
            AmenityViewState.AmenityCheckbox(
                id = amenityType.id,
                name = amenityType.name,
                iconDrawable = amenityType.iconDrawable,
                stringRes = amenityType.stringRes,
                isChecked = filterParamsMutableStateFlow.value.selectedAmenities.contains(amenityType),
                onCheckBoxClicked = EquatableCallbackWithParam { isChecked ->
                    if (filterParamsMutableStateFlow.value.selectedAmenities.contains(amenityType) && !isChecked) {
                        filterParamsMutableStateFlow.update {
                            it.copy(selectedAmenities = it.selectedAmenities - amenityType)
                        }
                    } else if (!filterParamsMutableStateFlow.value.selectedAmenities.contains(amenityType) && isChecked) {
                        filterParamsMutableStateFlow.update {
                            it.copy(selectedAmenities = it.selectedAmenities + amenityType)
                        }
                    }
                },
            )
        }

    fun onPropertyTypeSelected(propertyType: String) {
        filterParamsMutableStateFlow.update {
            it.copy(propertyType = propertyType)
        }
    }


    fun onEntryDateRangeStatusChanged(searchedEntryDateRange: SearchedEntryDateRange) {
        filterParamsMutableStateFlow.update {
            it.copy(searchedEntryDateRange = searchedEntryDateRange)
        }
    }

    fun onPropertySaleStateChanged(propertySateState: PropertySaleState) {
        filterParamsMutableStateFlow.update {
            it.copy(saleState = propertySateState)
        }
    }

    fun onResetFilters() {
        filterParamsMutableStateFlow.tryEmit(FilterParams())
    }

    fun onMinPriceChanged(minPrice: String?) {
        if (minPrice.isNullOrBlank()) {
            filterParamsMutableStateFlow.update {
                it.copy(minPrice = BigDecimal.ZERO)
            }
        } else
            filterParamsMutableStateFlow.update {
                it.copy(minPrice = BigDecimal(minPrice))
            }
    }

    fun onMaxPriceChanged(maxPrice: String?) {
        if (maxPrice.isNullOrBlank()) {
            filterParamsMutableStateFlow.update {
                it.copy(maxPrice = BigDecimal.ZERO)
            }
        } else
            filterParamsMutableStateFlow.update {
                it.copy(maxPrice = BigDecimal(maxPrice))
            }
    }

    fun onMinSurfaceChanged(minSurface: String?) {
        if (minSurface.isNullOrBlank()) {
            filterParamsMutableStateFlow.update {
                it.copy(minSurface = BigDecimal.ZERO)
            }
        } else
            filterParamsMutableStateFlow.update {
                it.copy(minSurface = BigDecimal(minSurface))
            }
    }

    fun onMaxSurfaceChanged(maxSurface: String?) {
        if (maxSurface.isNullOrBlank()) {
            filterParamsMutableStateFlow.update {
                it.copy(maxSurface = BigDecimal.ZERO)
            }
        } else
            filterParamsMutableStateFlow.update {
                it.copy(maxSurface = BigDecimal(maxSurface))
            }
    }
}

data class FilterParams(
    val propertyType: String? = null,
    val minPrice: BigDecimal = BigDecimal.ZERO,
    val maxPrice: BigDecimal = BigDecimal.ZERO,
    val minSurface: BigDecimal = BigDecimal.ZERO,
    val maxSurface: BigDecimal = BigDecimal.ZERO,
    val selectedAmenities: List<AmenityType> = emptyList(),
    val saleState: PropertySaleState? = null,
    val searchedEntryDateRange: SearchedEntryDateRange? = null,
)