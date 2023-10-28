package com.emplk.realestatemanager.domain.locale_formatting

import com.emplk.realestatemanager.domain.currency_rate.CurrencyRateRepository
import com.emplk.realestatemanager.domain.currency_rate.CurrencyRateWrapper
import java.math.BigDecimal
import java.util.Locale
import javax.inject.Inject

class ConvertToUsdDependingOnLocaleUseCase @Inject constructor(
    private val localeFormattingRepository: LocaleFormattingRepository,
    private val currencyRateRepository: CurrencyRateRepository,
    private val getLocaleUseCase: GetLocaleUseCase,
) {
    /**
     * @return BigDecimal of the price converted to USD depending on locale
     * (used to save USD in database)
     */
    suspend fun invoke(price: BigDecimal): BigDecimal {
        val locale = getLocaleUseCase.invoke()

        return if (locale == Locale.FRANCE) {
            when (val currencyWrapper = currencyRateRepository.getCurrentCurrencyRate()) {
                is CurrencyRateWrapper.Success -> {
                    localeFormattingRepository.convertEuroToDollar(
                        price,
                        currencyWrapper.currencyRateEntity.usdToEuroRate
                    )
                }

                is CurrencyRateWrapper.Error -> {
                    localeFormattingRepository.convertEuroToDollar(price, currencyWrapper.fallbackUsToEuroRate)
                }
            }
        } else {
            price
        }
    }
}