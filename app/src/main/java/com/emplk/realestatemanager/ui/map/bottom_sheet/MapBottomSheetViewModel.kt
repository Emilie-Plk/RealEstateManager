package com.emplk.realestatemanager.ui.map.bottom_sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceByLocaleUseCase
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceUnitByLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.FormatPriceByLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetRoundedSurfaceWithSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.property.type_price_surface.GetPropertyPriceTypeAndSurfaceByIdUseCase
import com.emplk.realestatemanager.ui.map.bottom_sheet.MapBottomSheetFragment.Companion.DETAIL_PROPERTY_TAG
import com.emplk.realestatemanager.ui.map.bottom_sheet.MapBottomSheetFragment.Companion.EDIT_PROPERTY_TAG
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@HiltViewModel
class MapBottomSheetViewModel @Inject constructor(
    private val getPropertyPriceTypeAndSurfaceByIdUseCase: GetPropertyPriceTypeAndSurfaceByIdUseCase,
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase,
    private val convertSurfaceUnitByLocaleUseCase: ConvertSurfaceUnitByLocaleUseCase,
    private val convertPriceByLocaleUseCase: ConvertPriceByLocaleUseCase,
    private val getRoundedSurfaceWithSurfaceUnitUseCase: GetRoundedSurfaceWithSurfaceUnitUseCase,
    private val formatPriceByLocaleUseCase: FormatPriceByLocaleUseCase,
) : ViewModel() {

    private val onActionClickedMutableSharedFlow: MutableSharedFlow<Pair<String, Long>> =
        MutableSharedFlow(extraBufferCapacity = 1)

    private val isProgressBarVisibleMutableLiveData: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val viewState: LiveData<PropertyMapBottomSheetViewState> = liveData {
        if (latestValue == null) isProgressBarVisibleMutableLiveData.tryEmit(true)
        getCurrentPropertyIdFlowUseCase.invoke().filterNotNull().collect { propertyId ->
            isProgressBarVisibleMutableLiveData.tryEmit(false)
            val currentProperty =
                getPropertyPriceTypeAndSurfaceByIdUseCase.invoke(propertyId)

            val propertyWithConvertedPriceAndSurface = currentProperty.copy(
                price = convertPriceByLocaleUseCase.invoke(currentProperty.price),
                surface = convertSurfaceUnitByLocaleUseCase.invoke(currentProperty.surface)
            )

            emit(
                PropertyMapBottomSheetViewState(
                    propertyId = propertyId,
                    type = propertyWithConvertedPriceAndSurface.type,
                    price = formatPriceByLocaleUseCase.invoke(propertyWithConvertedPriceAndSurface.price),
                    surface = getRoundedSurfaceWithSurfaceUnitUseCase.invoke(propertyWithConvertedPriceAndSurface.surface),
                    featuredPicture = NativePhoto.Uri(propertyWithConvertedPriceAndSurface.featuredPictureUri),
                    onDetailClick = EquatableCallbackWithParam {
                        onActionClickedMutableSharedFlow.tryEmit(Pair(DETAIL_PROPERTY_TAG, propertyId))
                    },
                    onEditClick = EquatableCallbackWithParam {
                        onActionClickedMutableSharedFlow.tryEmit(Pair(EDIT_PROPERTY_TAG, propertyId))
                    },
                    description = propertyWithConvertedPriceAndSurface.description,
                    rooms = NativeText.Argument(
                        R.string.rooms_nb_short_version,
                        propertyWithConvertedPriceAndSurface.rooms
                    ),
                    bedrooms = NativeText.Argument(
                        R.string.bedrooms_nb_short_version,
                        propertyWithConvertedPriceAndSurface.bedrooms
                    ),
                    bathrooms = NativeText.Argument(
                        R.string.bathrooms_nb_short_version,
                        propertyWithConvertedPriceAndSurface.bathrooms
                    ),
                    isProgressBarVisible = isProgressBarVisibleMutableLiveData.value
                )
            )
        }
    }

    val viewEvent: LiveData<Event<MapBottomSheetEvent>> = liveData {
        onActionClickedMutableSharedFlow.collect {
            when (it.first) {
                DETAIL_PROPERTY_TAG -> emit(Event(MapBottomSheetEvent.OnDetailClick(it.second)))
                EDIT_PROPERTY_TAG -> emit(Event(MapBottomSheetEvent.OnEditClick(it.second)))
            }
        }
    }

}

