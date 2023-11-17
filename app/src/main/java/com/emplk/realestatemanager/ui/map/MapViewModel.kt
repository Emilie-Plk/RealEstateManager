package com.emplk.realestatemanager.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.geolocation.GetCurrentLocationUseCase
import com.emplk.realestatemanager.domain.map.GetAllPropertiesLatLongUseCase
import com.emplk.realestatemanager.domain.permission.PermissionRepository
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
    private val permissionRepository: PermissionRepository,
) : ViewModel() {

    private val onClickMutableSharedFlow: MutableSharedFlow<Unit> = MutableSharedFlow(extraBufferCapacity = 1)

    val viewState: LiveData<MarkerViewState> = liveData {
        combine(
            getAllPropertiesLatLongUseCase.invoke(),
            getCurrentLocationUseCase.invoke(),
        ) { propertiesLatLong, currentLocation ->
            propertiesLatLong.map { propertyLatLong ->
                emit(
                    MarkerViewState(
                        userCurrentLocation = if (currentLocation != null) {
                            LatLng(currentLocation.latitude, currentLocation.longitude)
                        } else {
                            null
                        },
                        propertyMarkers = listOf(
                            PropertyMarkerViewState(
                                propertyId = propertyLatLong.propertyId,
                                latLng = propertyLatLong.latLng,
                                onMarkerClicked = EquatableCallbackWithParam { propertyId ->
                                    setCurrentPropertyIdUseCase.invoke(propertyId)
                                    onClickMutableSharedFlow.tryEmit(Unit)
                                },
                            )
                        )
                    )
                )
            }
        }.collect()
    }


    val viewEvent: LiveData<Event<MapEvent>> = liveData {
        onClickMutableSharedFlow.collect {
            emit(Event(MapEvent.OnMarkerClicked))
        }
    }
}
