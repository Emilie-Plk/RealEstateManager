package com.emplk.realestatemanager.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.amenity.AmenityType
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.locale_formatting.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.locale_formatting.SurfaceUnitType
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.GetPropertyByItsIdUseCase
import com.emplk.realestatemanager.domain.screen_width.GetScreenWidthTypeFlowUseCase
import com.emplk.realestatemanager.domain.screen_width.ScreenWidthType
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPropertyByItsIdUseCase: GetPropertyByItsIdUseCase,
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase,
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
    private val getScreenWidthTypeFlowUseCase: GetScreenWidthTypeFlowUseCase,
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    private val onEditButtonClickedMutableSharedFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val viewEventLiveData: LiveData<Event<DetailViewEvent>> = liveData(coroutineDispatcherProvider.io) {
        onEditButtonClickedMutableSharedFlow.collect {
            Log.d("COUCOU", "we enter the onEditButtonClickedMutableSharedFlow.collect")
            combine(
                getScreenWidthTypeFlowUseCase.invoke(),
                getCurrentPropertyIdFlowUseCase.invoke(),
            ) { screenWidthType, currentId ->
                Log.d("COUCOU DetailVM", "we enter the combine")
                if (currentId >= 0) {
                    when (screenWidthType) {
                        ScreenWidthType.TABLET -> {
                            emit(Event(DetailViewEvent.DisplayEditFragmentTablet))
                        }

                        ScreenWidthType.PHONE -> {
                            emit(Event(DetailViewEvent.NavigateToMainActivity))
                        }

                        // Should never occur
                        ScreenWidthType.UNDEFINED -> {
                        }
                    }
                }
            }.collect()
        }
    }

    val viewState: LiveData<DetailViewState> = liveData(coroutineDispatcherProvider.io) {
        getCurrentPropertyIdFlowUseCase.invoke().flatMapLatest { propertyId ->
            if (latestValue == null) {
                emit(DetailViewState.LoadingState)
            }
            getPropertyByItsIdUseCase.invoke(propertyId)
        }.collectLatest { propertyEntity ->
            val currencyType = getCurrencyTypeUseCase.invoke()
            val surfaceUnitType = getSurfaceUnitUseCase.invoke()

            emit(
                DetailViewState.PropertyDetail(
                    id = propertyEntity.id,
                    propertyType = propertyEntity.type,
                    featuredPicture = NativePhoto.Uri(
                        propertyEntity.pictures.first { picture ->
                            picture.isThumbnail
                        }.uri
                    ),
                    pictures = propertyEntity.pictures.map { picture ->
                        picture.uri
                    },
                    price = when (currencyType) {
                        CurrencyType.DOLLAR -> NativeText.Argument(
                            R.string.price_in_dollar,
                            propertyEntity.price
                        )

                        CurrencyType.EURO -> NativeText.Argument(
                            R.string.price_in_euro,
                            propertyEntity.price
                        )
                    },
                    surface = when (surfaceUnitType) {
                        SurfaceUnitType.SQUARE_FEET -> NativeText.Argument(
                            R.string.surface_in_square_feet,
                            propertyEntity.surface
                        )

                        SurfaceUnitType.SQUARE_METER -> NativeText.Argument(
                            R.string.surface_in_square_meters,
                            propertyEntity.surface
                        )
                    },
                    rooms = NativeText.Argument(
                        R.string.detail_number_of_room_textview,
                        propertyEntity.rooms
                    ),
                    bathrooms = NativeText.Argument(
                        R.string.detail_number_of_bathroom_textview,
                        propertyEntity.bathrooms
                    ),
                    bedrooms = NativeText.Argument(
                        R.string.detail_number_of_bedroom_textview,
                        propertyEntity.bedrooms
                    ),
                    description = propertyEntity.description,
                    address = NativeText.Arguments(
                        R.string.detail_location_tv,
                        listOf(
                            propertyEntity.location.address,
                            propertyEntity.location.postalCode,
                            propertyEntity.location.city,
                        )
                    ),
                    amenitySchool = propertyEntity.amenities.any {
                        it.type == AmenityType.SCHOOL
                    },
                    amenityPark = propertyEntity.amenities.any {
                        it.type == AmenityType.PARK
                    },
                    amenityShoppingMall = propertyEntity.amenities.any {
                        it.type == AmenityType.SHOPPING_MALL
                    },
                    amenityRestaurant = propertyEntity.amenities.any {
                        it.type == AmenityType.RESTAURANT
                    },
                    amenityConcierge = propertyEntity.amenities.any {
                        it.type == AmenityType.CONCIERGE
                    },
                    amenityPublicTransportation = propertyEntity.amenities.any {
                        it.type == AmenityType.PUBLIC_TRANSPORTATION
                    },
                    amenityHospital = propertyEntity.amenities.any {
                        it.type == AmenityType.HOSPITAL
                    },
                    amenityLibrary = propertyEntity.amenities.any {
                        it.type == AmenityType.LIBRARY
                    },
                    amenityGym = propertyEntity.amenities.any {
                        it.type == AmenityType.GYM
                    },
                    entryDate = NativeText.Argument(
                        R.string.detail_entry_date_tv,
                        propertyEntity.entryDate.format(
                            DateTimeFormatter.ofLocalizedDate(
                                FormatStyle.SHORT
                            )
                        )
                    ),
                    agentName = NativeText.Argument(
                        R.string.detail_manager_agent_name,
                        propertyEntity.agentName
                    ),
                    isSold = propertyEntity.isSold,
                    saleDate = propertyEntity.saleDate?.let { saleDate ->
                        NativeText.Argument(
                            R.string.detail_sold_date_tv,
                            saleDate.format(
                                DateTimeFormatter.ofLocalizedDate(
                                    FormatStyle.SHORT
                                )
                            )
                        )
                    }
                )
            )
        }
    }

    fun onEditClicked() {
        onEditButtonClickedMutableSharedFlow.tryEmit(Unit)
        setNavigationTypeUseCase.invoke(NavigationFragmentType.EDIT_FRAGMENT)
    }
}