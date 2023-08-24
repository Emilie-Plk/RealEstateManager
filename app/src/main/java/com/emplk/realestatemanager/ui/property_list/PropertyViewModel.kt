package com.emplk.realestatemanager.ui.property_list

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.add_property.AddPropertyUseCase
import com.emplk.realestatemanager.domain.currency.CurrencyType
import com.emplk.realestatemanager.domain.currency.GetCurrencyTypeFormattingUseCase
import com.emplk.realestatemanager.domain.entities.LocationEntity
import com.emplk.realestatemanager.domain.entities.PointOfInterestEntity
import com.emplk.realestatemanager.domain.entities.PropertyEntity
import com.emplk.realestatemanager.domain.entities.PropertyPictureEntity
import com.emplk.realestatemanager.domain.get_properties.GetPropertiesAsFlowUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class PropertyViewModel @Inject constructor(
    private val getPropertiesAsFlowUseCase: GetPropertiesAsFlowUseCase,
    private val addPropertyUseCase: AddPropertyUseCase,
    private val getCurrencyTypeFormattingUseCase: GetCurrencyTypeFormattingUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val resources: Resources,
) : ViewModel() {

    fun onAddPropertyClicked() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            addPropertyUseCase.invoke(
                PropertyEntity(
                    id = 0,
                    type = "Flat",
                    price = 100000,
                    surface = 150,
                    rooms = 5,
                    description = "Discover luxury living at its finest with this stunning and spacious home. Boasting elegant design, high-end finishes, and a prime location, this property offers everything you need for a comfortable and lavish lifestyle.",
                    photos = listOf(
                        PropertyPictureEntity(
                            uri = "https://img.zumpercdn.com/486165561/1280x960?fit=crop&h=300&w=300",
                            description = "Front view",
                            isThumbnail = true
                        ),
                        PropertyPictureEntity(
                            uri = "https://random.imagecdn.app/300/300",
                            description = "Back view",
                            isThumbnail = false
                        ),
                    ),
                    location = LocationEntity(
                        latitude = 40.765076,
                        longitude = -73.976693,
                        address = "Chambers Street",
                        city = "New York City",
                        neighborhood = "Midtown Manhattan",
                        postalCode = "10019",
                    ),
                    pointsOfInterest = listOf(
                        PointOfInterestEntity(
                            name = "School",
                            isSelected = true
                        )
                    ),
                    isAvailableForSale = true,
                    entryDate = LocalDateTime.of(2023, 8, 24, 10, 0),
                    saleDate = null,
                    isSold = false,
                    agent = "John Doe"
                ),
            )
        }
    }

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
        combine(
            getPropertiesAsFlowUseCase.invoke(),
            getCurrencyTypeFormattingUseCase.invoke()
        ) { properties, currencyType ->
            properties.map {
                PropertyViewState.Property(
                    id = it.id,
                    typeOfProperty = it.type,
                    featuredPicture = it.photos.find { photo -> photo.isThumbnail }?.uri ?: "",
                    address = it.location.address,
                    price = when (currencyType) {
                        CurrencyType.DOLLAR -> resources.getString(R.string.price_in_dollar, it.price)
                        CurrencyType.EURO -> resources.getString(R.string.price_in_euro, it.price)
                    },
                    isSold = it.isSold,
                    onClickEvent = EquatableCallback {
                        PropertyViewEvent.NavigateToDetailActivity(it.id)
                    }
                )
            }
        }.collect { emit(it) }
    }
}
