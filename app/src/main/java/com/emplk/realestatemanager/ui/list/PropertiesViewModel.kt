package com.emplk.realestatemanager.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.filter.GetPropertiesFilterFlowUseCase
import com.emplk.realestatemanager.domain.filter.IsPropertyMatchingFiltersUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertToUsdDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.FormatPriceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetRoundedSurfaceWithSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.GetPropertiesAsFlowUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PropertiesViewModel @Inject constructor(
    private val getPropertiesAsFlowUseCase: GetPropertiesAsFlowUseCase,
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase,
    private val convertSurfaceDependingOnLocaleUseCase: ConvertSurfaceDependingOnLocaleUseCase,
    private val convertPriceDependingOnLocaleUseCase: ConvertPriceDependingOnLocaleUseCase,
    private val getRoundedHumanReadableSurfaceUseCase: GetRoundedSurfaceWithSurfaceUnitUseCase,
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
            }

            val propertiesWithConvertedPriceAndSurface = properties.map { property ->
                val convertedPrice = convertPriceDependingOnLocaleUseCase.invoke(property.price)
                val convertedSurface = convertSurfaceDependingOnLocaleUseCase.invoke(property.surface)
                property.copy(
                    price = convertedPrice,
                    surface = convertedSurface
                )
            }
            emit(propertiesWithConvertedPriceAndSurface
                .asSequence()
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
                        price = property.price,
                        isSold = property.isSold,
                        room = NativeText.Argument(R.string.rooms_nb_short_version, property.rooms),
                        bathroom = NativeText.Argument(R.string.bathrooms_nb_short_version, property.bathrooms),
                        bedroom = NativeText.Argument(R.string.bedrooms_nb_short_version, property.bedrooms),
                        humanReadableSurface = getRoundedHumanReadableSurfaceUseCase.invoke(property.surface),
                        surface = property.surface,
                        entryDate = property.entryDate,
                        amenities = property.amenities,
                        onClickEvent = EquatableCallback {
                            setCurrentPropertyIdUseCase.invoke(property.id)
                            setNavigationTypeUseCase.invoke(NavigationFragmentType.DETAIL_FRAGMENT)
                        },
                    )
                }
                .sortedBy { it.isSold }
                .filter { property ->
                    if (propertiesFilter == null) return@filter true
                        isPropertyMatchingFiltersUseCase.invoke(
                            property.propertyType,
                            property.price,
                            property.surface,
                            property.amenities,
                            property.entryDate,
                            property.isSold,
                            propertiesFilter
                        )
                }
                .toList()
            )
        }.collect()
    }
}
