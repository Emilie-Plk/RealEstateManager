package com.emplk.realestatemanager.ui.map.bottom_sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.property.type_price_surface.GetPropertyPriceTypeAndSurfaceByIdUseCase
import com.emplk.realestatemanager.ui.map.bottom_sheet.MapBottomSheetFragment.Companion.DETAIL_PROPERTY_TAG
import com.emplk.realestatemanager.ui.map.bottom_sheet.MapBottomSheetFragment.Companion.EDIT_PROPERTY_TAG
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativePhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import javax.inject.Inject

@HiltViewModel
class MapBottomSheetViewModel @Inject constructor(
    private val getPropertyPriceTypeAndSurfaceByIdUseCase: GetPropertyPriceTypeAndSurfaceByIdUseCase,
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase,
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
            emit(
                PropertyMapBottomSheetViewState(
                    propertyId = propertyId,
                    type = currentProperty.type,
                    price = currentProperty.price.toString(),
                    surface = currentProperty.surface.toString(),
                    featuredPicture = NativePhoto.Uri(currentProperty.featuredPictureUri),
                    onDetailClick = EquatableCallbackWithParam {
                        onActionClickedMutableSharedFlow.tryEmit(Pair(DETAIL_PROPERTY_TAG, propertyId))
                    },
                    onEditClick = EquatableCallbackWithParam {
                        onActionClickedMutableSharedFlow.tryEmit(Pair(EDIT_PROPERTY_TAG, propertyId))
                    },
                    description = currentProperty.description,
                    rooms = currentProperty.rooms.toString(),
                    bedrooms = currentProperty.bedrooms.toString(),
                    bathrooms = currentProperty.bathrooms.toString(),
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

