package com.emplk.realestatemanager.ui.filter

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.domain.filter.GetEntryDateByEntryDateStatusUseCase
import com.emplk.realestatemanager.domain.filter.GetFilteredPropertiesUseCase
import com.emplk.realestatemanager.domain.filter.GetMinMaxPriceAndSurfaceUseCase
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property.amenity.type.GetAmenityTypeUseCase
import com.emplk.realestatemanager.domain.property_type.GetPropertyTypeUseCase
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.add.type.PropertyTypeViewStateItem
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
    private val getEntryDateByEntryDateStatusUseCase: GetEntryDateByEntryDateStatusUseCase,
    private val getMinMaxPriceAndSurfaceUseCase: GetMinMaxPriceAndSurfaceUseCase,
    private val getPropertyTypeUseCase: GetPropertyTypeUseCase,
    private val getAmenityTypeUseCase: GetAmenityTypeUseCase,
) : ViewModel() {

    private val filterParamsMutableStateFlow = MutableStateFlow(FilterParams())


    val viewState: LiveData<FilterViewState> = liveData {
        coroutineScope {


            launch {
                combine(
                    filterParamsMutableStateFlow,
                    getFilteredPropertiesUseCase.invoke(
                        propertyType = filterParamsMutableStateFlow.value.propertyType,
                        minPrice = filterParamsMutableStateFlow.value.minPrice,
                        maxPrice = filterParamsMutableStateFlow.value.maxPrice,
                        minSurface = filterParamsMutableStateFlow.value.minSurface,
                        maxSurface = filterParamsMutableStateFlow.value.maxSurface,
                        amenities = filterParamsMutableStateFlow.value.selectedAmenities,
                        entryDateMin = null,
                        entryDateMax = null,
                        isSold = filterParamsMutableStateFlow.value.isSold
                    ),
                ) { filterParams, filterePropertiesCount ->
                    val minMaxPriceAndSurface = getMinMaxPriceAndSurfaceUseCase.invoke()
                    val propertyTypes = getPropertyTypeUseCase.invoke()
                    val amenityTypes = getAmenityTypeUseCase.invoke()

                    Log.d("COUCOU", "filteredIds: $filterePropertiesCount")
                    FilterViewState(
                        type = filterParams.propertyType,
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
                        entryDate = filterParams.entryDateStatus,
                        availableForSale = filterParams.isSold == false,
                        // if filteredIds count is 0, it means that the user didn't set any filter
                        filterButtonText = if (filterePropertiesCount == 0) {
                            NativeText.Simple("None")
                        } else {
                            NativeText.Simple("$filterePropertiesCount properties")
                        },
                        onCancelClicked = EquatableCallback { filterParamsMutableStateFlow.update { FilterParams() } },
                        onFilterClicked = EquatableCallback {
                        }
                    )
                }.collectLatest { emit(it) }
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


    fun onEntryDateStatusChanged(entryDateStatus: EntryDateStatus) {
        filterParamsMutableStateFlow.update {
            it.copy(
                entryDateStatus = entryDateStatus
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
    val isSold: Boolean? = false,
    val entryDateStatus: EntryDateStatus = EntryDateStatus.NONE,
)