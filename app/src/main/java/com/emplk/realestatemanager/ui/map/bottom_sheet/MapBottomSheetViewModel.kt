package com.emplk.realestatemanager.ui.map.bottom_sheet

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceByLocaleUseCase
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.FormatPriceByLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetRoundedSurfaceWithSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.property.GetCurrentPropertyUseCase
import com.emplk.realestatemanager.domain.property.pictures.PictureEntity
import com.emplk.realestatemanager.ui.map.bottom_sheet.MapBottomSheetFragment.Companion.DETAIL_PROPERTY_TAG
import com.emplk.realestatemanager.ui.map.bottom_sheet.MapBottomSheetFragment.Companion.EDIT_PROPERTY_TAG
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParams
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
    private val convertSurfaceDependingOnLocaleUseCase: ConvertSurfaceDependingOnLocaleUseCase,
    private val convertPriceByLocaleUseCase: ConvertPriceByLocaleUseCase,
    private val getRoundedSurfaceWithSurfaceUnitUseCase: GetRoundedSurfaceWithSurfaceUnitUseCase,
    private val formatPriceByLocaleUseCase: FormatPriceByLocaleUseCase,
) : ViewModel() {

    private val onActionClickedMutableSharedFlow: MutableSharedFlow<Pair<Long, String>> =
        MutableSharedFlow(extraBufferCapacity = 1)
    // tODO: virer le long

    private val isProgressBarVisibleMutableLiveData: MutableStateFlow<Boolean> = MutableStateFlow(false)
    // TODO: 2021-08-31  virer ça

    val viewState: LiveData<PropertyMapBottomSheetViewState> = liveData {
        if (latestValue == null) isProgressBarVisibleMutableLiveData.tryEmit(true)
        getCurrentPropertyUseCase.invoke().collectLatest { property ->
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
                    onDetailClick = EquatableCallbackWithParams { propertyId, fragmentTag ->
                        onActionClickedMutableSharedFlow.tryEmit((propertyId to fragmentTag))
                    },
                    onEditClick = EquatableCallbackWithParams { propertyId, fragmentTag ->
                        onActionClickedMutableSharedFlow.tryEmit((propertyId to fragmentTag))
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
            when (it.second) {
                EDIT_PROPERTY_TAG -> emit(Event(MapBottomSheetEvent.Edit(it.first)))
                DETAIL_PROPERTY_TAG -> emit(Event(MapBottomSheetEvent.Detail))
            }
        }
    }
}

