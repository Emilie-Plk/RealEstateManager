package com.emplk.realestatemanager.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceByLocaleUseCase
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.FormatPriceByLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetRoundedSurfaceWithSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.GetPropertiesAsFlowUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PropertiesViewModel @Inject constructor(
    private val getPropertiesAsFlowUseCase: GetPropertiesAsFlowUseCase,
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase,
    private val convertSurfaceDependingOnLocaleUseCase: ConvertSurfaceDependingOnLocaleUseCase,
    private val convertPriceByLocaleUseCase: ConvertPriceByLocaleUseCase,
    private val getRoundedSurfaceWithSurfaceUnitUseCase: GetRoundedSurfaceWithSurfaceUnitUseCase,
    private val formatPriceByLocaleUseCase: FormatPriceByLocaleUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
) : ViewModel() {

    val viewState: LiveData<List<PropertiesViewState>> = liveData {
        if (latestValue == null) {
            emit(listOf(PropertiesViewState.LoadingState))
        }

        getPropertiesAsFlowUseCase.invoke().collect { properties ->
            if (properties.isEmpty()) {
                emit(listOf(PropertiesViewState.EmptyState(
                    onAddClick = EquatableCallback {
                        setNavigationTypeUseCase.invoke(NavigationFragmentType.ADD_FRAGMENT)
                    }
                )))
                return@collect
            }

            val propertiesWithConvertedPriceAndSurface = properties.map { property ->
                val convertedPrice = convertPriceByLocaleUseCase.invoke(property.price)
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
                        price = formatPriceByLocaleUseCase.invoke(property.price),
                        isSold = property.isSold,
                        room = NativeText.Argument(R.string.rooms_nb_short_version, property.rooms),
                        bathroom = NativeText.Argument(R.string.bathrooms_nb_short_version, property.bathrooms),
                        bedroom = NativeText.Argument(R.string.bedrooms_nb_short_version, property.bedrooms),
                        surface = getRoundedSurfaceWithSurfaceUnitUseCase.invoke(property.surface),
                        onClickEvent = EquatableCallback {
                            setCurrentPropertyIdUseCase.invoke(property.id)
                            setNavigationTypeUseCase.invoke(NavigationFragmentType.DETAIL_FRAGMENT)
                        }
                    )
                }
                .sortedBy { it.isSold }
                .toList()
            )
        }
    }
}
