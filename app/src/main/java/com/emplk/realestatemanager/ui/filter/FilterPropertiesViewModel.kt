package com.emplk.realestatemanager.ui.filter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.filter.GetEntryDateByEntryDateStatusUseCase
import com.emplk.realestatemanager.domain.filter.GetFilteredPropertiesUseCase
import com.emplk.realestatemanager.domain.filter.GetMinMaxPriceAndSurfaceUseCase
import com.emplk.realestatemanager.domain.filter.GetPropertyTypeForFilterUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertToUsdDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.FormatPriceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetRoundedSurfaceWithSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property.amenity.type.GetAmenityTypeUseCase
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.add.type.PropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

@HiltViewModel
class FilterPropertiesViewModel @Inject constructor(
    private val getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase,
    private val getEntryDateByEntryDateStatusUseCase: GetEntryDateByEntryDateStatusUseCase,
    private val getMinMaxPriceAndSurfaceUseCase: GetMinMaxPriceAndSurfaceUseCase,
    private val formatPriceToHumanReadableUseCase: FormatPriceToHumanReadableUseCase,
    private val getRoundedSurfaceWithSurfaceUnitUseCase: GetRoundedSurfaceWithSurfaceUnitUseCase,
    private val convertSurfaceDependingOnLocaleUseCase: ConvertSurfaceDependingOnLocaleUseCase,
    private val convertPriceDependingOnLocaleUseCase: ConvertPriceDependingOnLocaleUseCase,
    private val convertToUsdDependingOnLocaleUseCase: ConvertToUsdDependingOnLocaleUseCase,
    private val convertSurfaceToSquareFeetDependingOnLocaleUseCase: ConvertSurfaceToSquareFeetDependingOnLocaleUseCase,
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
    private val getPropertyTypeForFilterUseCase: GetPropertyTypeForFilterUseCase,
    private val getAmenityTypeUseCase: GetAmenityTypeUseCase,
) : ViewModel() {

    private val filterParamsMutableStateFlow = MutableStateFlow(FilterParams())

    val viewState: LiveData<FilterViewState> = liveData {

        filterParamsMutableStateFlow.flatMapLatest { filterParams ->
            getFilteredPropertiesUseCase.invoke(
                propertyType = filterParams.propertyType,
                minPrice = convertToUsdDependingOnLocaleUseCase.invoke(filterParams.minPrice),
                maxPrice = convertToUsdDependingOnLocaleUseCase.invoke(filterParams.maxPrice),
                minSurface = convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(filterParams.minSurface),
                maxSurface = convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(filterParams.maxSurface),
                amenities = filterParams.selectedAmenities,
                entryDateMin = getEntryDateByEntryDateStatusUseCase.invoke(filterParams.entryDateState)?.first,
                entryDateMax = getEntryDateByEntryDateStatusUseCase.invoke(filterParams.entryDateState)?.second,
                propertySaleState = filterParams.saleState,
            )
        }.collectLatest { filteredPropertiesCount ->
            val minMaxPriceAndSurface = getMinMaxPriceAndSurfaceUseCase.invoke()

            val propertyTypes = getPropertyTypeForFilterUseCase.invoke()
            val amenityTypes = getAmenityTypeUseCase.invoke()

            Log.d("COUCOU", "viewState: $filteredPropertiesCount")
            Log.d("COUCOU", "minSurface converted: ${convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(filterParamsMutableStateFlow.value.minSurface)}" +
                    "maxSurface converted: ${convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(filterParamsMutableStateFlow.value.maxSurface)}" +
            "surface converted: min ${convertSurfaceDependingOnLocaleUseCase.invoke(minMaxPriceAndSurface.minSurface)}" +
                    "max ${convertSurfaceDependingOnLocaleUseCase.invoke(minMaxPriceAndSurface.maxSurface)}")
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
                        convertSurfaceDependingOnLocaleUseCase.invoke(minMaxPriceAndSurface.minSurface)
                            .setScale(0, RoundingMode.HALF_UP).intValueExact(),
                        convertSurfaceDependingOnLocaleUseCase.invoke(minMaxPriceAndSurface.maxSurface)
                            .setScale(0, RoundingMode.HALF_UP).intValueExact(),
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
                entryDate = filterParamsMutableStateFlow.value.entryDateState,
                availableForSale = filterParamsMutableStateFlow.value.saleState,
                filterButtonText = when (filteredPropertiesCount) {
                    0 -> {
                        NativeText.Simple("None")
                    }

                    1 -> {
                        NativeText.Simple("1 property")
                    }

                    else -> {
                        NativeText.Simple("$filteredPropertiesCount properties")
                    }
                },
                onCancelClicked = EquatableCallback { filterParamsMutableStateFlow.update { FilterParams() } },
                isFilterButtonEnabled = filterParamsMutableStateFlow.value != FilterParams() || filteredPropertiesCount != 0,
                onFilterClicked = EquatableCallback {
                }
            )
            )
            Log.d("COUCOU", "viewState: ${filterParamsMutableStateFlow.value}")
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


    fun onEntryDateStatusChanged(entryDateState: EntryDateState) {
        filterParamsMutableStateFlow.update {
            it.copy(entryDateState = entryDateState)
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
    val saleState: PropertySaleState = PropertySaleState.ALL,
    val entryDateState: EntryDateState = EntryDateState.NONE,
)