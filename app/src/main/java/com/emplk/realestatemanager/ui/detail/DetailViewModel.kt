package com.emplk.realestatemanager.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.currency_rate.GetLastUpdatedCurrencyRateDateUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.currency.FormatPriceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.FormatAndRoundSurfaceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.map_picture.GenerateMapUrlWithApiKeyUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.GetCurrentPropertyUseCase
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.detail.picture_banner.PictureBannerViewState
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getCurrentPropertyUseCase: GetCurrentPropertyUseCase,
    private val formatPriceToHumanReadableUseCase: FormatPriceToHumanReadableUseCase,
    private val convertPriceDependingOnLocaleUseCase: ConvertPriceDependingOnLocaleUseCase,
    private val getLastUpdatedCurrencyRateDateUseCase: GetLastUpdatedCurrencyRateDateUseCase,
    private val getLocaleUseCase: GetLocaleUseCase,
    private val formatAndRoundSurfaceToHumanReadableUseCase: FormatAndRoundSurfaceToHumanReadableUseCase,
    private val convertToSquareFeetDependingOnLocaleUseCase: ConvertToSquareFeetDependingOnLocaleUseCase,
    private val generateMapUrlWithApiKeyUseCase: GenerateMapUrlWithApiKeyUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
) : ViewModel() {

    companion object {
        private val US = Locale.US
        private val FRANCE = Locale.FRANCE
    }

    val viewState: LiveData<DetailViewState> = liveData {
        if (latestValue == null) {
            emit(DetailViewState.LoadingState)
        }

        getCurrentPropertyUseCase.invoke().collect { property ->
            emit(DetailViewState.PropertyDetail(
                id = property.id,
                propertyType = property.type,
                pictures = property.pictures
                    .sortedByDescending { it.isFeatured }
                    .mapIndexed { index, picture ->
                        PictureBannerViewState(
                            pictureUri = NativePhoto.Uri(picture.uri),
                            description = picture.description,
                            picturePosition = index + 1,
                            pictureNumberText = NativeText.Arguments(
                                R.string.banner_pic_number,
                                listOf((index + 1).toString(), property.pictures.size.toString())
                            ),
                            isFeatured = picture.isFeatured
                        )
                    }.sortedBy { it.picturePosition },
                mapMiniature = NativePhoto.Uri(
                    generateMapUrlWithApiKeyUseCase.invoke(
                        property.location.miniatureMapUrl
                    )
                ),
                price = formatPriceToHumanReadableUseCase.invoke(
                    convertPriceDependingOnLocaleUseCase.invoke(property.price)
                ),
                lastUpdatedCurrencyRateDate =
                getLastUpdatedCurrencyRateDateUseCase.invoke()?.let {
                    NativeText.Argument(
                        R.string.currency_rate_last_updated_date_tv,
                        it
                    )
                } ?: NativeText.Resource(R.string.currency_rate_with_no_date_tv),
                isCurrencyLastUpdatedCurrencyRateVisible = when (getLocaleUseCase.invoke()) {
                    US -> false
                    FRANCE -> true
                    else -> false
                },
                surface = formatAndRoundSurfaceToHumanReadableUseCase.invoke(
                    convertToSquareFeetDependingOnLocaleUseCase.invoke(property.surface)
                ),
                rooms = NativeText.Argument(
                    R.string.detail_number_of_room_textview,
                    property.rooms
                ),
                bathrooms = NativeText.Argument(
                    R.string.detail_number_of_bathroom_textview,
                    property.bathrooms
                ),
                bedrooms = NativeText.Argument(
                    R.string.detail_number_of_bedroom_textview,
                    property.bedrooms
                ),
                description = property.description,
                address = NativeText.Argument(
                    R.string.detail_location_tv,
                    property.location.address
                ),
                amenities = property.amenities.map { amenity ->
                    AmenityViewState.AmenityItem(
                        stringRes = amenity.stringRes,
                        iconDrawable = amenity.iconDrawable,
                    )
                },
                entryDate = NativeText.Argument(
                    R.string.detail_entry_date_tv,
                    property.entryDate.format(
                        DateTimeFormatter.ofLocalizedDate(
                            FormatStyle.SHORT
                        )
                    )
                ),
                agentName = NativeText.Argument(
                    R.string.detail_manager_agent_name,
                    property.agentName
                ),
                isSold = property.saleDate != null,
                saleDate = property.saleDate?.let { saleDate ->
                    NativeText.Argument(
                        R.string.detail_sold_date_tv,
                        saleDate.format(
                            DateTimeFormatter.ofLocalizedDate(
                                FormatStyle.SHORT
                            )
                        )
                    )
                }
            ))
        }
    }

    fun onEditClicked() {
        setNavigationTypeUseCase.invoke(NavigationFragmentType.EDIT_FRAGMENT)
    }
}