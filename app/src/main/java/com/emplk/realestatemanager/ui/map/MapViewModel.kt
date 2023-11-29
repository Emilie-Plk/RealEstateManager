package com.emplk.realestatemanager.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.geolocation.GeolocationState
import com.emplk.realestatemanager.domain.geolocation.GetCurrentLocationUseCase
import com.emplk.realestatemanager.domain.map.GetAllPropertiesLatLongUseCase
import com.emplk.realestatemanager.domain.permission.SetLocationPermissionUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.Event
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getAllPropertiesLatLongUseCase: GetAllPropertiesLatLongUseCase,
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase,
    private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
    private val setLocationPermissionUseCase: SetLocationPermissionUseCase,
) : ViewModel() {

    companion object {
        /**
         * Default is Google HQ location, could be changed to user's last known location or any other chosen location
         */
        private val FALLBACK_LOCATION = LatLng(37.422131, -122.084801)
    }

    private val eventMutableSharedFlow: MutableSharedFlow<MapEvent> = MutableSharedFlow(extraBufferCapacity = 1)

    val viewState: LiveData<MarkerViewState> = liveData {
        if (latestValue == null) {
            emit(
                MarkerViewState(
                    userCurrentLocation = null,
                    fallbackLocationGoogleHq = FALLBACK_LOCATION,
                    propertyMarkers = emptyList()
                )
            )
        }

        combine(
            getAllPropertiesLatLongUseCase.invoke(),
            getCurrentLocationUseCase.invoke()
        ) { propertiesLatLong, geolocationState ->
            MarkerViewState(
                userCurrentLocation = when (geolocationState) {
                    is GeolocationState.Success -> LatLng(
                        geolocationState.latitude,
                        geolocationState.longitude
                    )

                    is GeolocationState.Error -> {
                        eventMutableSharedFlow.tryEmit(MapEvent.Toast(geolocationState.message))
                        null
                    }

                    /*  is GeolocationState.LastKnownLocation -> LatLng(
                          geolocationState.latitude,
                          geolocationState.longitude
                      )*/
                },
                fallbackLocationGoogleHq = FALLBACK_LOCATION,
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
        }.collectLatest { emit(it) }
    }

    val viewEvent: LiveData<Event<MapEvent>> = liveData {
        eventMutableSharedFlow.collect {
            emit(Event(it))
        }
    }

    fun hasPermissionBeenGranted(isPermissionGranted: Boolean?) {
        setLocationPermissionUseCase.invoke(isPermissionGranted ?: false)
    }
}
