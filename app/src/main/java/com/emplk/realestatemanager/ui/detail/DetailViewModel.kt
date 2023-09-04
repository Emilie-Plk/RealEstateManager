package com.emplk.realestatemanager.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.amenity.Amenity
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.get_properties.GetPropertyByItsIdUseCase
import com.emplk.realestatemanager.domain.locale_formatting.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.locale_formatting.SurfaceUnitType
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.screen_width.GetScreenWidthTypeFlowUseCase
import com.emplk.realestatemanager.domain.screen_width.ScreenWidthType
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
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

    val viewEventLiveData: LiveData<Event<DetailViewEvent>> = liveData {
        combine(
            getScreenWidthTypeFlowUseCase.invoke(),
            getCurrentPropertyIdFlowUseCase.invoke(),
        ) { screenWidthType, currentId ->
            if (currentId >= 0) {
                when (screenWidthType) {
                    ScreenWidthType.TABLET ->
                        emit(
                            Event(
                                DetailViewEvent.DisplayEditFragmentTablet(
                                    currentId
                                )
                            )
                        )

                    ScreenWidthType.PHONE ->
                        emit(
                            Event(
                                DetailViewEvent.DisplayEditFragmentPhone(
                                    currentId
                                )
                            )
                        )

                    ScreenWidthType.UNDEFINED -> {
                        emit(
                            Event(
                                DetailViewEvent.DisplayEditFragmentPhone(
                                    currentId
                                )
                            )
                        )
                    }
                }
            }
        }.collect()
    }

    val viewState: LiveData<DetailViewState> = liveData {
        getCurrentPropertyIdFlowUseCase.invoke().collect { propertyId ->
            getPropertyByItsIdUseCase.invoke(propertyId).collect {
                val currencyType = getCurrencyTypeUseCase.invoke()
                val surfaceUnitType = getSurfaceUnitUseCase.invoke()
                emit(
                    DetailViewState(
                        id = it.property.id,
                        type = it.property.type,
                        featuredPicture = NativePhoto.Uri(
                            it.pictures.first { picture ->
                                picture.isThumbnail
                            }.uri
                        ),
                        pictures = it.pictures.map { picture ->
                            picture.uri
                        },
                        price = when (currencyType) {
                            CurrencyType.DOLLAR -> NativeText.Argument(
                                R.string.price_in_dollar,
                                it.property.price
                            )

                            CurrencyType.EURO -> NativeText.Argument(
                                R.string.price_in_euro,
                                it.property.price
                            )
                        },
                        surface = when (surfaceUnitType) {
                            SurfaceUnitType.SQUARE_FEET -> NativeText.Argument(
                                R.string.surface_in_square_feet,
                                it.property.surface
                            )

                            SurfaceUnitType.SQUARE_METER -> NativeText.Argument(
                                R.string.surface_in_square_meters,
                                it.property.surface
                            )
                        },
                        rooms = NativeText.Argument(
                            R.string.detail_number_of_room_textview,
                            it.property.rooms
                        ),
                        bathrooms = NativeText.Argument(
                            R.string.detail_number_of_bathroom_textview,
                            it.property.bathrooms
                        ),
                        bedrooms = NativeText.Argument(
                            R.string.detail_number_of_bedroom_textview,
                            it.property.bedrooms
                        ),
                        description = it.property.description,
                        address = NativeText.Arguments(
                            R.string.detail_location_tv,
                            listOf(
                                it.location.neighborhood,
                                it.location.address,
                                it.location.postalCode,
                                it.location.city,
                            )
                        ),
                        amenitySchool = it.property.amenities.contains(Amenity.SCHOOL),
                        amenityPark = it.property.amenities.contains(Amenity.PARK),
                        amenityShoppingMall = it.property.amenities.contains(Amenity.SHOPPING_MALL),
                        amenityRestaurant = it.property.amenities.contains(Amenity.RESTAURANT),
                        amenityConcierge = it.property.amenities.contains(Amenity.CONCIERGE),
                        amenityPublicTransportation = it.property.amenities.contains(Amenity.PUBLIC_TRANSPORTATION),
                        amenityHospital = it.property.amenities.contains(Amenity.HOSPITAL),
                        amenityLibrary = it.property.amenities.contains(Amenity.LIBRARY),
                        entryDate = NativeText.Argument(
                            R.string.detail_entry_date_tv,
                            it.property.entryDate.format(
                                DateTimeFormatter.ofLocalizedDate(
                                    FormatStyle.SHORT
                                )
                            )
                        ),
                        agentName = NativeText.Argument(
                            R.string.detail_manager_agent_name,
                            it.property.agentName
                        ),
                        isSold = it.property.isSold,
                        saleDate = it.property.saleDate?.let { saleDate ->
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
    }

    fun onEditClicked() {
        setNavigationTypeUseCase.invoke(NavigationFragmentType.EDIT_FRAGMENT)
    }
}