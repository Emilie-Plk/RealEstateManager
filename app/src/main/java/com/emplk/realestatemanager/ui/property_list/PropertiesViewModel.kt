package com.emplk.realestatemanager.ui.property_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.locale_formatting.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.locale_formatting.SurfaceUnitType
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.GetPropertiesAsFlowUseCase
import com.emplk.realestatemanager.domain.screen_width.GetScreenWidthTypeFlowUseCase
import com.emplk.realestatemanager.domain.screen_width.ScreenWidthType
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertiesViewModel @Inject constructor(
    private val getPropertiesAsFlowUseCase: GetPropertiesAsFlowUseCase,
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase,
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
    private val getScreenWidthTypeFlowUseCase: GetScreenWidthTypeFlowUseCase,
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
) : ViewModel() {

    private val propertyIdMutableSharedFlow = MutableSharedFlow<Long>()

    val viewEventLiveData: LiveData<Event<PropertiesViewEvent>> = liveData {
        combine(
            propertyIdMutableSharedFlow,
            getScreenWidthTypeFlowUseCase.invoke()
        ) { propertyId, screenWidthType ->
            if (propertyId >= 0) {
                when (screenWidthType) {
                    ScreenWidthType.TABLET -> {
                        emit(
                            Event(
                                PropertiesViewEvent.DisplayDetailFragment
                            )
                        )
                    }

                    ScreenWidthType.PHONE -> {
                        emit(
                            Event(
                                PropertiesViewEvent.NavigateToDetailActivity
                            )
                        )
                    }

                    ScreenWidthType.UNDEFINED -> {
                        // Nothing to do
                    }
                }
            }
        }.collect()
    }

    val viewState: LiveData<List<PropertiesViewState>> = liveData {
        val currencyType = getCurrencyTypeUseCase.invoke()
        val surfaceUnitType = getSurfaceUnitUseCase.invoke()
        if (latestValue == null) {
            emit(listOf(PropertiesViewState.EmptyState))
        }
        getPropertiesAsFlowUseCase.invoke().collect { properties ->
            if (properties.isEmpty()) {
                emit(listOf(PropertiesViewState.EmptyState))
                return@collect
            }
            emit(
                properties.map { property ->
                    val photoUrl =
                        property.pictures.find { picture -> picture.isThumbnail }?.uri

                    PropertiesViewState.Properties(
                        id = property.id,
                        propertyType = property.type,
                        featuredPicture = if (photoUrl != null) {
                            NativePhoto.Uri(photoUrl)
                        } else {
                            NativePhoto.Resource(R.drawable.baseline_villa_24)
                        },
                        address = property.location.city,
                        price = when (currencyType) {
                            CurrencyType.DOLLAR -> NativeText.Argument(
                                R.string.price_in_dollar,
                                property.price
                            )

                            CurrencyType.EURO -> NativeText.Argument(
                                R.string.price_in_euro,
                                property.price
                            )
                        },
                        isSold = property.isSold,
                        room = property.rooms.toString(),
                        bathroom = property.bathrooms.toString(),
                        bedroom = property.bedrooms.toString(),
                        surface = when (surfaceUnitType) {
                            SurfaceUnitType.SQUARE_FEET -> NativeText.Argument(
                                R.string.surface_in_square_feet,
                                property.surface
                            )

                            SurfaceUnitType.SQUARE_METER -> NativeText.Argument(
                                R.string.surface_in_square_meters,
                                property.surface
                            )
                        },
                        onClickEvent = EquatableCallback {
                            viewModelScope.launch {
                                propertyIdMutableSharedFlow.emit(property.id)
                                setNavigationTypeUseCase.invoke(NavigationFragmentType.DETAIL_FRAGMENT)
                                setCurrentPropertyIdUseCase.invoke(property.id)
                            }
                        }
                    )
                }.sortedBy { it.isSold }
            )
        }
    }
}
