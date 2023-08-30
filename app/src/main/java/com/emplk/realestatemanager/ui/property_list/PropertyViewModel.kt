package com.emplk.realestatemanager.ui.property_list

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.get_properties.GetPropertiesAsFlowUseCase
import com.emplk.realestatemanager.domain.locale_formatting.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.locale_formatting.SurfaceUnitType
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertyViewModel @Inject constructor(
    private val getPropertiesAsFlowUseCase: GetPropertiesAsFlowUseCase,
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase,
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
) : ViewModel() {

    private val propertyIdMutableSharedFlow = MutableSharedFlow<Long>()

    val viewEventLiveData: LiveData<Event<PropertyViewEvent>> = liveData(Dispatchers.IO) {
        propertyIdMutableSharedFlow.collect { propertyId ->
            if (propertyId >= 0) {
                emit(
                    Event(
                        PropertyViewEvent.NavigateToDetailActivity(propertyId)
                    )
                )
                Log.d("COUCOU", "NavigateToDetailActivity($propertyId)")
            }
        }
    }

    val viewState: LiveData<List<PropertyViewState>> = liveData(Dispatchers.IO) {
        val currencyType = getCurrencyTypeUseCase.invoke()
        val surfaceUnitType = getSurfaceUnitUseCase.invoke()

        getPropertiesAsFlowUseCase.invoke().collect { properties ->
            emit(
                properties.map { propertiesWithPicturesAndLocation ->
                    val photoUrl =
                        propertiesWithPicturesAndLocation.pictures.find { picture -> picture.isThumbnail }?.uri

                    PropertyViewState.Property(
                        id = propertiesWithPicturesAndLocation.property.id,
                        propertyType = propertiesWithPicturesAndLocation.property.type,
                        featuredPicture = if (photoUrl != null) {
                            NativePhoto.Uri(photoUrl)
                        } else {
                            NativePhoto.Resource(R.drawable.baseline_villa_24)
                        },
                        address = propertiesWithPicturesAndLocation.location.address,
                        price = when (currencyType) {
                            CurrencyType.DOLLAR -> NativeText.Arguments(
                                R.string.price_in_dollar,
                                listOf(propertiesWithPicturesAndLocation.property.price)
                            )

                            CurrencyType.EURO -> NativeText.Arguments(
                                R.string.price_in_euro,
                                listOf(propertiesWithPicturesAndLocation.property.price)
                            )
                        },
                        isSold = propertiesWithPicturesAndLocation.property.isSold,
                        room = propertiesWithPicturesAndLocation.property.rooms.toString(),
                        bathroom = propertiesWithPicturesAndLocation.property.bathrooms.toString(),
                        bedroom = propertiesWithPicturesAndLocation.property.bedrooms.toString(),
                        surface = when (surfaceUnitType) {
                            SurfaceUnitType.SQUARE_FEET -> NativeText.Arguments(
                                R.string.surface_in_square_feet,
                                listOf(propertiesWithPicturesAndLocation.property.surface)
                            )

                            SurfaceUnitType.SQUARE_METER -> NativeText.Arguments(
                                R.string.surface_in_square_meters,
                                listOf(propertiesWithPicturesAndLocation.property.surface)
                            )
                        },
                        onClickEvent = EquatableCallback {
                            viewModelScope.launch {
                                propertyIdMutableSharedFlow.emit(propertiesWithPicturesAndLocation.property.id)
                            }
                        }
                    )
                }
            )
        }
    }
}
