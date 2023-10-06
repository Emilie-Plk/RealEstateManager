package com.emplk.realestatemanager.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.locale_formatting.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.locale_formatting.SurfaceUnitType
import com.emplk.realestatemanager.domain.map_picture.GenerateMapUrlWithApiKeyUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.GetPropertyByItsIdUseCase
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.detail.picture_banner.PictureBannerViewState
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPropertyByItsIdUseCase: GetPropertyByItsIdUseCase,
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase,
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase,
    private val generateMapUrlWithApiKeyUseCase: GenerateMapUrlWithApiKeyUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
) : ViewModel() {

    val viewState: LiveData<DetailViewState> = liveData {
        getCurrentPropertyIdFlowUseCase.invoke()
            .filterNotNull()
            .flatMapLatest { propertyId ->
                if (latestValue == null) {
                    emit(DetailViewState.LoadingState)
                }
                getPropertyByItsIdUseCase.invoke(propertyId)
            }.collectLatest { propertyEntity ->
                val currencyType = getCurrencyTypeUseCase.invoke()
                val surfaceUnitType = getSurfaceUnitUseCase.invoke()

                emit(DetailViewState.PropertyDetail(
                    id = propertyEntity.id,
                    propertyType = propertyEntity.type,
                    pictures = propertyEntity.pictures.map { picture ->
                        PictureBannerViewState(
                            pictureUri = NativePhoto.Uri(picture.uri),
                            description = picture.description,
                            isFeatured = picture.isFeatured
                        )
                    }.sortedByDescending { it.isFeatured },
                    mapMiniature = NativePhoto.Uri(
                        generateMapUrlWithApiKeyUseCase.invoke(
                            propertyEntity.location.miniatureMapUrl
                        )
                    ),
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
                    address = NativeText.Argument(
                        R.string.detail_location_tv,
                        propertyEntity.location.address
                    ),
                    amenities = propertyEntity.amenities.map {
                        AmenityViewState.AmenityItem(
                            stringRes = it.type.stringRes,
                            iconDrawable = it.type.iconDrawable,
                        )
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
        setNavigationTypeUseCase.invoke(NavigationFragmentType.EDIT_FRAGMENT)
    }
}