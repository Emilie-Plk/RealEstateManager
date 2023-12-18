package com.emplk.realestatemanager.domain.locale_formatting.surface

import com.emplk.realestatemanager.domain.currency_rate.CurrencyRateRepository
import com.emplk.realestatemanager.domain.currency_rate.CurrencyRateWrapper
import com.emplk.realestatemanager.domain.locale_formatting.GetLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.HumanReadableRepository
import java.math.BigDecimal
import java.util.Locale
import javax.inject.Inject

class ConvertToUsdDependingOnLocaleUseCase @Inject constructor(
    private val humanReadableRepository: HumanReadableRepository,
    private val currencyRateRepository: CurrencyRateRepository,
    private val getLocaleUseCase: GetLocaleUseCase,
) {
    /**
     * @return BigDecimal of the price converted to USD depending on locale
     * (used to save USD in database)
     */
    suspend fun invoke(price: BigDecimal): BigDecimal {
        val locale = getLocaleUseCase.invoke()
        if (price == BigDecimal.ZERO) return BigDecimal.ZERO

        return if (locale == Locale.FRANCE) {
            when (val currencyWrapper = currencyRateRepository.getCurrentCurrencyRate()) {
                is CurrencyRateWrapper.Success -> {
                    humanReadableRepository.convertEuroToDollarRoundedHalfUp(
                        price,
                        currencyWrapper.currencyRateEntity.usdToEuroRate
                    )
                }

                is CurrencyRateWrapper.Error -> {
                    humanReadableRepository.convertEuroToDollarRoundedHalfUp(
                        price,
                        currencyWrapper.fallbackUsToEuroRate
                    )
                }
            }
        } else {
            price
        }
    }
}
