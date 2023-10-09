package com.emplk.realestatemanager.ui.property_list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.currency_rate.CurrencyRateWrapper
import com.emplk.realestatemanager.domain.currency_rate.GetCurrencyRateUseCase
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceUnitByLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.FormatPriceByLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.locale_formatting.SurfaceUnitType
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.GetPropertiesAsFlowUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@HiltViewModel
class PropertiesViewModel @Inject constructor(
    private val getPropertiesAsFlowUseCase: GetPropertiesAsFlowUseCase,
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase,
    private val convertSurfaceUnitByLocaleUseCase: ConvertSurfaceUnitByLocaleUseCase,
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase,
    private val formatPriceByLocaleUseCase: FormatPriceByLocaleUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
) : ViewModel() {

    val viewState: LiveData<List<PropertiesViewState>> = liveData {
        if (latestValue == null) {
            emit(listOf(PropertiesViewState.LoadingState))
        }

        val surfaceUnitType = getSurfaceUnitUseCase.invoke()

        combine(
            getPropertiesAsFlowUseCase.invoke(),
            flow {
                emit(getCurrencyRateUseCase.invoke())
            },
        ) { properties, currencyRateWrapper ->
            if (properties.isEmpty()) {
                emit(listOf(PropertiesViewState.EmptyState))
                return@combine
            }


            emit(properties.asSequence()
                .map { property ->
                    val photoUrl = property.pictures.find { it.isFeatured }?.uri
                    val featuredPicture = photoUrl?.let { NativePhoto.Uri(it) }
                        ?: NativePhoto.Resource(R.drawable.baseline_villa_24)

                    val surfaceText = when (surfaceUnitType) {
                        SurfaceUnitType.SQUARE_FOOT -> NativeText.Argument(
                            R.string.surface_in_square_feet,
                            String.format("%.0f", property.surface)
                        )

                        SurfaceUnitType.SQUARE_METER -> NativeText.Argument(
                            R.string.surface_in_square_meters,
                            String.format(
                                "%.0f",
                                convertSurfaceUnitByLocaleUseCase.invoke(property.surface)
                            )
                        )
                    }

                    val price = when (currencyRateWrapper) {
                        is CurrencyRateWrapper.Success -> formatPriceByLocaleUseCase.invoke(
                            property.price,
                            currencyRateWrapper.currencyRateEntity.usdToEuroRate
                        )

                        is CurrencyRateWrapper.Error -> TODO()
                    }

                    PropertiesViewState.Properties(
                        id = property.id,
                        propertyType = property.type,
                        featuredPicture = featuredPicture,
                        address = property.location.address,
                        price = price,
                        isSold = property.isSold,
                        room = property.rooms.toString(),
                        bathroom = property.bathrooms.toString(),
                        bedroom = property.bedrooms.toString(),
                        surface = surfaceText,
                        onClickEvent = EquatableCallback {
                            setNavigationTypeUseCase.invoke(NavigationFragmentType.DETAIL_FRAGMENT)
                            setCurrentPropertyIdUseCase.invoke(property.id)
                        }
                    )
                }
                .sortedBy { it.isSold }
                .toList()
            )
        }.collect()
    }
}
