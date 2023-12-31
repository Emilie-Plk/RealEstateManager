package com.emplk.realestatemanager.ui.map.bottom_sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.currency.FormatPriceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.FormatAndRoundSurfaceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.GetCurrentPropertyUseCase
import com.emplk.realestatemanager.domain.property.pictures.PictureEntity
import com.emplk.realestatemanager.ui.map.bottom_sheet.MapBottomSheetFragment.Companion.DETAIL_PROPERTY_TAG
import com.emplk.realestatemanager.ui.map.bottom_sheet.MapBottomSheetFragment.Companion.EDIT_PROPERTY_TAG
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject

@HiltViewModel
class MapBottomSheetViewModel @Inject constructor(
    private val getCurrentPropertyUseCase: GetCurrentPropertyUseCase,
    private val convertToSquareFeetDependingOnLocaleUseCase: ConvertToSquareFeetDependingOnLocaleUseCase,
    private val convertPriceDependingOnLocaleUseCase: ConvertPriceDependingOnLocaleUseCase,
    private val getRoundedSurfaceWithUnitHumanReadableUseCase: FormatAndRoundSurfaceToHumanReadableUseCase,
    private val formatPriceToHumanReadableUseCase: FormatPriceToHumanReadableUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
) : ViewModel() {

    private val onActionClickedMutableSharedFlow: MutableSharedFlow<String> = MutableSharedFlow(extraBufferCapacity = 1)

    private val isProgressBarVisibleMutableLiveData: MutableStateFlow<Boolean> = MutableStateFlow(false)

    val viewState: LiveData<PropertyMapBottomSheetViewState> = liveData {
        if (latestValue == null) isProgressBarVisibleMutableLiveData.tryEmit(true)
        getCurrentPropertyUseCase.invoke().collectLatest { property ->
            isProgressBarVisibleMutableLiveData.tryEmit(false)

            val propertyWithConvertedPriceAndSurface = property.copy(
                price = convertPriceDependingOnLocaleUseCase.invoke(property.price),
                surface = convertToSquareFeetDependingOnLocaleUseCase.invoke(property.surface)
            )

            emit(
                PropertyMapBottomSheetViewState(
                    propertyId = propertyWithConvertedPriceAndSurface.id,
                    type = propertyWithConvertedPriceAndSurface.type,
                    price = formatPriceToHumanReadableUseCase.invoke(propertyWithConvertedPriceAndSurface.price),
                    surface = getRoundedSurfaceWithUnitHumanReadableUseCase.invoke(propertyWithConvertedPriceAndSurface.surface),
                    featuredPicture = NativePhoto.Uri(getFeaturedPictureUri(propertyWithConvertedPriceAndSurface.pictures)),
                    isSold = propertyWithConvertedPriceAndSurface.saleDate != null,
                    onDetailClick = EquatableCallbackWithParam { fragmentTag ->
                        setNavigationTypeUseCase.invoke(NavigationFragmentType.DETAIL_FRAGMENT)
                        onActionClickedMutableSharedFlow.tryEmit(fragmentTag)
                    },
                    onEditClick = EquatableCallbackWithParam { fragmentTag ->
                        setNavigationTypeUseCase.invoke(NavigationFragmentType.EDIT_FRAGMENT)
                        onActionClickedMutableSharedFlow.tryEmit(fragmentTag)
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
                )
            )
        }
    }

    private fun getFeaturedPictureUri(pictures: List<PictureEntity>): String = pictures.first { it.isFeatured }.uri


    val viewEvent: LiveData<Event<MapBottomSheetEvent>> = liveData {
        onActionClickedMutableSharedFlow.collect {
            when (it) {
                EDIT_PROPERTY_TAG -> emit(Event(MapBottomSheetEvent.Edit))
                DETAIL_PROPERTY_TAG -> emit(Event(MapBottomSheetEvent.Detail))
            }
        }
    }
}

