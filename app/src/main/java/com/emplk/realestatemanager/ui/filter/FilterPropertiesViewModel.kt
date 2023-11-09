package com.emplk.realestatemanager.ui.filter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.domain.filter.GetEntryDateByEntryDateStatusUseCase
import com.emplk.realestatemanager.domain.filter.GetFilteredPropertiesUseCase
import com.emplk.realestatemanager.domain.filter.GetMinMaxPriceAndSurfaceUseCase
import com.emplk.realestatemanager.domain.filter.GetPropertyTypeForFilterUseCase
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property.amenity.type.GetAmenityTypeUseCase
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.add.type.PropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
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
    private val getFilteredPropertiesUseCase: GetFilteredPropertiesUseCase,
    private val getEntryDateByEntryDateStatusUseCase: GetEntryDateByEntryDateStatusUseCase,
    private val getMinMaxPriceAndSurfaceUseCase: GetMinMaxPriceAndSurfaceUseCase,
    private val getPropertyTypeForFilterUseCase: GetPropertyTypeForFilterUseCase,
    private val getAmenityTypeUseCase: GetAmenityTypeUseCase,
) : ViewModel() {

    private val filterParamsMutableStateFlow = MutableStateFlow(FilterParams())


    val viewState: LiveData<FilterViewState> = liveData {
        coroutineScope {
            launch {
                filterParamsMutableStateFlow.flatMapLatest { filterParams ->
                    getFilteredPropertiesUseCase.invoke(
                        propertyType = filterParams.propertyType,
                        minPrice = filterParams.minPrice,
                        maxPrice = filterParams.maxPrice,
                        minSurface = filterParams.minSurface,
                        maxSurface = filterParams.maxSurface,
                        amenities = filterParams.selectedAmenities,
                        entryDateMin = null,
                        entryDateMax = null,
                        propertySaleState = filterParams.saleState,
                    )
                }.collectLatest { filteredPropertiesCount ->
                    val minMaxPriceAndSurface = getMinMaxPriceAndSurfaceUseCase.invoke()
                    val propertyTypes = getPropertyTypeForFilterUseCase.invoke()
                    val amenityTypes = getAmenityTypeUseCase.invoke()

                    Log.d("COUCOU", "filteredIds: $filteredPropertiesCount")
                    emit(FilterViewState(
                        type = filterParamsMutableStateFlow.value.propertyType,
                        minPrice = minMaxPriceAndSurface.minPrice.setScale(0, RoundingMode.HALF_UP).intValueExact(),
                        maxPrice = minMaxPriceAndSurface.maxPrice.setScale(0, RoundingMode.HALF_UP).intValueExact(),
                        minSurface = minMaxPriceAndSurface.minSurface.setScale(0, RoundingMode.HALF_UP)
                            .intValueExact(),
                        maxSurface = minMaxPriceAndSurface.maxSurface.setScale(0, RoundingMode.HALF_UP)
                            .intValueExact(),
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
                        onFilterClicked = EquatableCallback {
                        }
                    )
                    )
                }
            }
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
            it.copy(
                entryDateState = entryDateState
            )
        }
    }

    fun onPropertySaleStateChanged(propertySateState: PropertySaleState) {
        filterParamsMutableStateFlow.update {
            it.copy(
                saleState = propertySateState
            )
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