package com.emplk.realestatemanager.domain.locale_formatting

import com.emplk.realestatemanager.domain.currency_rate.ConvertDollarToEuroUseCase
import com.emplk.realestatemanager.domain.currency_rate.GetCurrencyRateUseCase
import java.math.BigDecimal
import java.util.Locale
import javax.inject.Inject

class FormatPriceByLocaleUseCase @Inject constructor(
    private val localeFormattingRepository: LocaleFormattingRepository,
    private val convertDollarToEuroUseCase: ConvertDollarToEuroUseCase,
) {
     fun invoke(priceInUsd: BigDecimal, usdToEuroRate: Double): String =
        when (localeFormattingRepository.getLocale()) {
            Locale.US ->
                localeFormattingRepository.formatPrice(priceInUsd)

            Locale.FRANCE -> localeFormattingRepository.formatPrice(
                convertDollarToEuroUseCase.invoke(
                    priceInUsd,
                    usdToEuroRate
                )
            )
            // Default to USD
            else -> localeFormattingRepository.formatPrice(priceInUsd)
        }
}