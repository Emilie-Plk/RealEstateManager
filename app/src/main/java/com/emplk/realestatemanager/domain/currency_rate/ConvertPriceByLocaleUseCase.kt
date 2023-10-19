package com.emplk.realestatemanager.domain.currency_rate

import com.emplk.realestatemanager.domain.locale_formatting.GetLocaleUseCase
import java.math.BigDecimal
import java.util.Locale
import javax.inject.Inject

class ConvertPriceByLocaleUseCase @Inject constructor(
    private val getCurrencyRateUseCase: GetCurrencyRateUseCase,
    private val convertDollarToEuroUseCase: ConvertDollarToEuroUseCase,
    private val getLocaleUseCase: GetLocaleUseCase,
) {

    companion object {
        private val FRANCE = Locale.FRANCE
    }

    /**
     * @return BigDecimal of the price converted to USD depending on locale
     * (used to convert price in € if Locale is France)
     */
    suspend fun invoke(price: BigDecimal): BigDecimal {
        when (getLocaleUseCase.invoke()) {
            FRANCE -> {
                return when (val currencyRateWrapper = getCurrencyRateUseCase.invoke()) {
                    is CurrencyRateWrapper.Success -> {
                        convertDollarToEuroUseCase.invoke(
                            price,
                            currencyRateWrapper.currencyRateEntity.usdToEuroRate
                        )
                    }

                    is CurrencyRateWrapper.Error -> {
                        convertDollarToEuroUseCase.invoke(
                            price,
                            currencyRateWrapper.fallbackUsToEuroRate
                        )
                    }
                }
            }

            else -> {
                return price
            }
        }
    }
}