package com.emplk.realestatemanager.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.map.GetAllPropertiesLatLongUseCase
import com.emplk.realestatemanager.domain.navigation.GetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
    private val getAllPropertiesLatLongUseCase: GetAllPropertiesLatLongUseCase,
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase,
) : ViewModel() {

    private val clickedPropertyIdMutableStateFlow: MutableStateFlow<Long?> = MutableStateFlow(null)

    val viewState: LiveData<List<MarkerViewState>> = liveData {
        viewModelScope.launch {
            emit(
                getAllPropertiesLatLongUseCase.invoke().map {
                    MarkerViewState(
                        propertyId = it.propertyId,
                        latLng = it.latLng,
                        onMarkerClicked = EquatableCallbackWithParam { propertyId ->
                            setCurrentPropertyIdUseCase.invoke(propertyId)
                        }
                    )
                })
        }.join()
    }

    val viewEventLiveData: LiveData<Event<MapEvent>> = liveData {
        viewModelScope.launch {
            clickedPropertyIdMutableStateFlow.collect {
                it?.let { propertyId ->
                    emit(Event(MapEvent.OnMarkerClicked(propertyId)))
                }
            }
        }.join()
    }

    fun onMarkerClicked(propertyId: Long) {
        clickedPropertyIdMutableStateFlow.tryEmit(propertyId)
    }
}
