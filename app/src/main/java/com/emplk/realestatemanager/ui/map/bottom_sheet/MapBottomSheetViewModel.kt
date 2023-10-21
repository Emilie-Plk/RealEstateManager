package com.emplk.realestatemanager.ui.map.bottom_sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceByLocaleUseCase
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.FormatPriceByLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetRoundedSurfaceWithSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.GetPropertyByItsIdAsFlowUseCase
import com.emplk.realestatemanager.domain.property.pictures.PictureEntity
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class MapBottomSheetViewModel @Inject constructor(
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase,
    private val getPropertyByItsIdAsFlowUseCase: GetPropertyByItsIdAsFlowUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val convertSurfaceDependingOnLocaleUseCase: ConvertSurfaceDependingOnLocaleUseCase,
    private val convertPriceByLocaleUseCase: ConvertPriceByLocaleUseCase,
    private val getRoundedSurfaceWithSurfaceUnitUseCase: GetRoundedSurfaceWithSurfaceUnitUseCase,
    private val formatPriceByLocaleUseCase: FormatPriceByLocaleUseCase,
) : ViewModel() {

    private val onActionClickedMutableSharedFlow: MutableSharedFlow<String> =
        MutableSharedFlow(extraBufferCapacity = 1)

    private val isProgressBarVisibleMutableLiveData: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val viewState: LiveData<PropertyMapBottomSheetViewState> = liveData {
        if (latestValue == null) isProgressBarVisibleMutableLiveData.tryEmit(true)
        getCurrentPropertyIdFlowUseCase.invoke().filterNotNull().flatMapLatest { propertyId ->
            getPropertyByItsIdAsFlowUseCase.invoke(propertyId)
        }.collectLatest { property ->
            isProgressBarVisibleMutableLiveData.tryEmit(false)

            val propertyWithConvertedPriceAndSurface = property.copy(
                price = convertPriceByLocaleUseCase.invoke(property.price),
                surface = convertSurfaceDependingOnLocaleUseCase.invoke(property.surface)
            )

            emit(
                PropertyMapBottomSheetViewState(
                    propertyId = propertyWithConvertedPriceAndSurface.id,
                    type = propertyWithConvertedPriceAndSurface.type,
                    price = formatPriceByLocaleUseCase.invoke(propertyWithConvertedPriceAndSurface.price),
                    surface = getRoundedSurfaceWithSurfaceUnitUseCase.invoke(propertyWithConvertedPriceAndSurface.surface),
                    featuredPicture = NativePhoto.Uri(getFeaturedPictureUri(propertyWithConvertedPriceAndSurface.pictures)),
                    onDetailClick = EquatableCallback {
                        setNavigationTypeUseCase.invoke(NavigationFragmentType.DETAIL_FRAGMENT)
                    },
                    onEditClick = EquatableCallback {
                        setNavigationTypeUseCase.invoke(NavigationFragmentType.EDIT_FRAGMENT)
                    },
                    description = propertyWithConvertedPriceAndSurface.description,
                    rooms = NativeText.Argument(
                        R.string.rooms_nb_short_version,
                        propertyWithConvertedPriceAndSurface.rooms
                    ),
                    bedrooms = NativeText.Argument(
                        R.string.bedrooms_nb_short_version,
                        propertyWithConvertedPriceAndSurface.bedrooms
                    ),
                    bathrooms = NativeText.Argument(
                        R.string.bathrooms_nb_short_version,
                        propertyWithConvertedPriceAndSurface.bathrooms
                    ),
                    isProgressBarVisible = isProgressBarVisibleMutableLiveData.value,

                    amenities = propertyWithConvertedPriceAndSurface.amenities.map { amenity ->
                        AmenityViewState.AmenityItem(
                            stringRes = amenity.stringRes,
                            iconDrawable = amenity.iconDrawable,
                        )
                    },
                )
            )
        }
    }

    private fun getFeaturedPictureUri(pictures: List<PictureEntity>): String = pictures.first { it.isFeatured }.uri


    /*val viewEvent: LiveData<Event<MapBottomSheetEvent>> = liveData {
        onActionClickedMutableSharedFlow.collect {
            when (it) {
                DETAIL_PROPERTY_TAG -> emit(Event(MapBottomSheetEvent.OnDetailClick(NavigationFragmentType.DETAIL_FRAGMENT.name)))
                EDIT_PROPERTY_TAG -> emit(Event(MapBottomSheetEvent.OnEditClick(NavigationFragmentType.EDIT_FRAGMENT.name)))
            }
        }
    }*/
}

