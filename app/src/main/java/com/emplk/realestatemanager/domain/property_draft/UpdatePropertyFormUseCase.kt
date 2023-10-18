package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.currency_rate.ConvertEuroToDollarUseCase
import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceByLocaleUseCase
import com.emplk.realestatemanager.domain.currency_rate.CurrencyRateWrapper
import com.emplk.realestatemanager.domain.currency_rate.GetCurrencyRateUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceUnitByLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetLocaleUseCase
import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.math.BigDecimal
import java.util.Locale
import javax.inject.Inject

class UpdatePropertyFormUseCase @Inject constructor(
    private val formDraftRepository: FormDraftRepository,
    private val convertSurfaceUnitByLocaleUseCase: ConvertSurfaceUnitByLocaleUseCase,
    private val convertEuroToDollarUseCase: ConvertEuroToDollarUseCase,
    private val convertPriceByLocaleUseCase: ConvertPriceByLocaleUseCase,
    private val getLocaleUseCase: GetLocaleUseCase,
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase,
) {
    suspend fun invoke(form: FormDraftStateEntity) {
        coroutineScope {
            val currencyWrapperDeferred = async {
                getCurrencyRateUseCase.invoke()
            }
            formDraftRepository.update(
                FormDraftEntity(
                    id = form.id,
                    type = form.propertyType,
                    price = if (form.price > BigDecimal.ZERO) {
                        when (getLocaleUseCase.invoke()) {
                            Locale.US -> form.price
                            Locale.FRANCE -> when (val currencyRateEntity = currencyWrapperDeferred.await()) {
                                is CurrencyRateWrapper.Success -> convertEuroToDollarUseCase.invoke(
                                    form.price,
                                    currencyRateEntity.currencyRateEntity.usdToEuroRate
                                )

                                is CurrencyRateWrapper.Error -> convertEuroToDollarUseCase.invoke(
                                    form.price,
                                    currencyRateEntity.fallbackUsToEuroRate
                                )
                            }

                            else -> throw IllegalStateException("Locale not supported")
                        }
                    } else {
                        form.price
                    },
                    surface = if (form.surface != null && form.surface.toDouble() > 0.0) {
                        convertSurfaceUnitByLocaleUseCase.invoke(form.surface.toDouble())
                    } else {
                        null
                    },
                    description = form.description,
                    rooms = form.nbRooms,
                    bathrooms = form.nbBathrooms,
                    address = form.address,
                    isAddressValid = form.isAddressValid,
                    bedrooms = form.nbBedrooms,
                    agentName = form.agent,
                    amenities = form.amenities.map { amenity ->
                        AmenityEntity(
                            id = amenity.id,
                            type = amenity.type,
                        )
                    },
                )
            )
        }
    }
}