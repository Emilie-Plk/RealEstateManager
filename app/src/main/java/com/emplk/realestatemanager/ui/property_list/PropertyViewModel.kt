package com.emplk.realestatemanager.ui.property_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.get_properties.GetPropertiesAsFlowUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class PropertyViewModel @Inject constructor(
    private val getPropertiesAsFlowUseCase: GetPropertiesAsFlowUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    val viewEventLiveData: LiveData<Event<PropertyViewEvent>> = liveData {
        getPropertiesAsFlowUseCase.invoke()
            .map { properties ->
                properties.map { property ->
                    Event(PropertyViewEvent.NavigateToDetailActivity(id = property.id))
                }
            }
            .collect { events ->
                events.forEach { emit(it) }
            }
    }

    val viewState: LiveData<List<PropertyViewState>> = liveData(Dispatchers.IO) {
        getPropertiesAsFlowUseCase.invoke()
            .map { properties ->
                if (properties.isEmpty()) {
                    listOf(PropertyViewState.EmptyState)
                } else {
                    properties.map {
                        PropertyViewState.Property(
                            id = it.id,
                            typeOfProperty = it.type,
                            featuredPicture = it.photos.find { photo -> photo.isThumbnail }?.uri ?: "",
                            address = it.address,
                            price = it.price.toString(),
                            onClickEvent = EquatableCallback {
                                PropertyViewEvent.NavigateToDetailActivity(it.id)
                            }
                        )
                    }
                }
            }
            .collect { emit(it) }
    }
}
