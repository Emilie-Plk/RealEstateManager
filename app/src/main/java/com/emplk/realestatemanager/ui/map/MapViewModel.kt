package com.emplk.realestatemanager.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.map.GetAllPropertiesLatLongUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getAllPropertiesLatLongUseCase: GetAllPropertiesLatLongUseCase,
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase,
) : ViewModel() {



    private val onClickMutableSharedFlow: MutableSharedFlow<Unit> = MutableSharedFlow(extraBufferCapacity = 1)

    val viewState: LiveData<List<MarkerViewState>> = liveData {
        emit(
            getAllPropertiesLatLongUseCase.invoke().map {
                MarkerViewState(
                    propertyId = it.propertyId,
                    latLng = it.latLng,
                    onMarkerClicked = EquatableCallbackWithParam { propertyId ->
                        setCurrentPropertyIdUseCase.invoke(propertyId)
                        onClickMutableSharedFlow.tryEmit(Unit)
                    }
                )
            })
    }

    val viewEventLiveData: LiveData<Event<MapEvent>> = liveData {
        onClickMutableSharedFlow.collect {
            emit(Event(MapEvent.OnMarkerClicked))
        }
    }
}
