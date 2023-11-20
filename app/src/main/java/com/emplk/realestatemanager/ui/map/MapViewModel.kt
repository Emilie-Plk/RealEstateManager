package com.emplk.realestatemanager.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.geolocation.GeolocationState
import com.emplk.realestatemanager.domain.geolocation.GetCurrentLocationUseCase
import com.emplk.realestatemanager.domain.map.GetAllPropertiesLatLongUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.Event
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getAllPropertiesLatLongUseCase: GetAllPropertiesLatLongUseCase,
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
) : ViewModel() {

    private val eventMutableSharedFlow: MutableSharedFlow<MapEvent> = MutableSharedFlow(extraBufferCapacity = 1)

    private val hasPermissionBeenGrantedMutableStateFlow: MutableStateFlow<Boolean?> =
        MutableStateFlow(null)

    val viewState: LiveData<MarkerViewState> = liveData {
        combine(
            getAllPropertiesLatLongUseCase.invoke(),
            getCurrentLocationUseCase.invoke(hasPermissionBeenGrantedMutableStateFlow),
        ) { propertiesLatLong, geolocationState ->
            emit(MarkerViewState(
                userCurrentLocation = when (geolocationState) {
                    is GeolocationState.Success -> LatLng(
                        geolocationState.latitude,
                        geolocationState.longitude
                    )

                    else -> null
                },
                propertyMarkers = if (propertiesLatLong.isEmpty()) emptyList() else
                    propertiesLatLong.map { propertyLatLong ->
                        PropertyMarkerViewState(
                            propertyId = propertyLatLong.propertyId,
                            latLng = propertyLatLong.latLng,
                            onMarkerClicked = EquatableCallbackWithParam { propertyId ->
                                setCurrentPropertyIdUseCase.invoke(propertyId)
                                eventMutableSharedFlow.tryEmit(MapEvent.OnMarkerClicked)
                            },
                        )
                    }
            )
            )
        }.collect()
    }

    val viewEvent: LiveData<Event<MapEvent>> = liveData {
        eventMutableSharedFlow.collect {
            emit(Event(it))
        }
    }

    fun hasPermissionBeenGranted(isPermissionGranted: Boolean?) {
        hasPermissionBeenGrantedMutableStateFlow.tryEmit(isPermissionGranted)
    }
}
