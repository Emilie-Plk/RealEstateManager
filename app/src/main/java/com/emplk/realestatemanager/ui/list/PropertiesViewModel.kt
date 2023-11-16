package com.emplk.realestatemanager.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.filter.GetPropertiesFilterFlowUseCase
import com.emplk.realestatemanager.domain.filter.IsPropertyMatchingFiltersUseCase
import com.emplk.realestatemanager.domain.locale_formatting.currency.FormatPriceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.FormatAndRoundSurfaceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.GetPropertiesAsFlowUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@HiltViewModel
class PropertiesViewModel @Inject constructor(
    private val getPropertiesAsFlowUseCase: GetPropertiesAsFlowUseCase,
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase,
    private val convertToSquareFeetDependingOnLocaleUseCase: ConvertToSquareFeetDependingOnLocaleUseCase,
    private val convertPriceDependingOnLocaleUseCase: ConvertPriceDependingOnLocaleUseCase,
    private val getRoundedHumanReadableSurfaceUseCase: FormatAndRoundSurfaceToHumanReadableUseCase,
    private val formatPriceToHumanReadableUseCase: FormatPriceToHumanReadableUseCase,
    private val getPropertiesFilterFlowUseCase: GetPropertiesFilterFlowUseCase,
    private val isPropertyMatchingFiltersUseCase: IsPropertyMatchingFiltersUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
) : ViewModel() {

    val viewState: LiveData<List<PropertiesViewState>> = liveData {
        if (latestValue == null) {
            emit(listOf(PropertiesViewState.LoadingState))
        }

        combine(
            getPropertiesAsFlowUseCase.invoke(),
            getPropertiesFilterFlowUseCase.invoke()
        ) { properties, propertiesFilter ->
            if (properties.isEmpty()) {
                emit(
                    listOf(
                        PropertiesViewState.EmptyState(
                            onAddClick = EquatableCallback {
                                setNavigationTypeUseCase.invoke(NavigationFragmentType.ADD_FRAGMENT)
                            }
                        )))
            } else {
                val propertiesWithConvertedPriceAndSurface = properties.map { property ->
                    val convertedPrice = convertPriceDependingOnLocaleUseCase.invoke(property.price)
                    val convertedSurface = convertToSquareFeetDependingOnLocaleUseCase.invoke(property.surface)
                    property.copy(
                        price = convertedPrice,
                        surface = convertedSurface
                    )
                }
                emit(propertiesWithConvertedPriceAndSurface
                    .asSequence()
                    .filter { property ->
                        isPropertyMatchingFiltersUseCase.invoke(
                            property.type,
                            property.price,
                            property.surface,
                            property.amenities,
                            property.entryDate,
                            property.isSold,
                            propertiesFilter
                        )
                    }
                    .sortedBy { it.isSold }
                    .map { property ->
                        val photoUrl = property.pictures.find { it.isFeatured }?.uri
                        val featuredPicture = photoUrl?.let { NativePhoto.Uri(it) }
                            ?: NativePhoto.Resource(R.drawable.baseline_villa_24)
                        PropertiesViewState.Properties(
                            id = property.id,
                            propertyType = property.type,
                            featuredPicture = featuredPicture,
                            address = property.location.address,
                            humanReadablePrice = formatPriceToHumanReadableUseCase.invoke(property.price),
                            isSold = property.isSold,
                            room = NativeText.Argument(R.string.rooms_nb_short_version, property.rooms),
                            bathroom = NativeText.Argument(R.string.bathrooms_nb_short_version, property.bathrooms),
                            bedroom = NativeText.Argument(R.string.bedrooms_nb_short_version, property.bedrooms),
                            humanReadableSurface = getRoundedHumanReadableSurfaceUseCase.invoke(property.surface),
                            entryDate = property.entryDate,
                            amenities = property.amenities,
                            onClickEvent = EquatableCallback {
                                setCurrentPropertyIdUseCase.invoke(property.id)
                                setNavigationTypeUseCase.invoke(NavigationFragmentType.DETAIL_FRAGMENT)
                            },
                        )
                    }
                    .toList()
                )
            }
        }.collect()
    }
}
