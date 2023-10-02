package com.emplk.realestatemanager.ui.map.bottom_sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.domain.property.type_price_surface.GetPropertyPriceTypeAndSurfaceByIdUseCase
import com.emplk.realestatemanager.ui.map.bottom_sheet.MapBottomSheetFragment.Companion.DETAIL_PROPERTY_TAG
import com.emplk.realestatemanager.ui.map.bottom_sheet.MapBottomSheetFragment.Companion.EDIT_PROPERTY_TAG
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativePhoto
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapBottomSheetViewModel @Inject constructor(
    private val getPropertyPriceTypeAndSurfaceByIdUseCase: GetPropertyPriceTypeAndSurfaceByIdUseCase,
) : ViewModel() {
    val propertyIdMutableStateFlow: MutableStateFlow<Long?> = MutableStateFlow(null)

    private val onActionClickedMutableSharedFlow: MutableSharedFlow<Pair<String, Long>> =
        MutableSharedFlow(extraBufferCapacity = 1)

    val viewState: LiveData<PropertyMapBottomSheetViewState> = liveData {
        propertyIdMutableStateFlow.value?.let { propertyId ->
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
                )
            )
        }
    }

    val viewEvent: LiveData<Event<MapBottomSheetEvent>> = liveData {
        viewModelScope.launch {
            onActionClickedMutableSharedFlow.collect {
                when (it.first) {
                    DETAIL_PROPERTY_TAG -> {
                        propertyIdMutableStateFlow.value?.let { propertyId ->
                            emit(Event(MapBottomSheetEvent.OnDetailClick(propertyId)))
                        }
                    }

                    EDIT_PROPERTY_TAG -> {
                        propertyIdMutableStateFlow.value?.let { propertyId ->
                            emit(Event(MapBottomSheetEvent.OnEditClick(propertyId)))
                        }
                    }
                }
            }
        }
    }
}

