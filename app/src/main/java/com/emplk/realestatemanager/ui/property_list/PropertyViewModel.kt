package com.emplk.realestatemanager.ui.property_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.get_properties.GetPropertiesAsFlowUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyViewModel @Inject constructor(
    private val getPropertiesAsFlowUseCase: GetPropertiesAsFlowUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    val viewState: LiveData<List<PropertyViewState>> = liveData(Dispatchers.IO) {
        getPropertiesAsFlowUseCase.invoke()
            .flatMapLatest { properties ->
                if (properties.isEmpty()) {
                    flowOf(listOf(PropertyViewState.EmptyState))
                } else {
                    flowOf(
                        properties.map {
                            PropertyViewState.Property(
                                id = it.id,
                                typeOfProperty = it.type,
                                featuredPicture = it.photos.find { photo -> photo.isThumbnail }?.uri ?: "",
                                address = it.address,
                                price = it.price.toString(),
                                onClickEvent = EquatableCallback {
                                        PropertyViewEvent.NavigateToDetailActivity
                                }
                            )
                        }
                    )
                }
            }
            .collect { emit(it) }
    }
}
