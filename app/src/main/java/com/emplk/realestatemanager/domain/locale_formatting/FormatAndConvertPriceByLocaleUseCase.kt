package com.emplk.realestatemanager.domain.locale_formatting

import java.math.BigDecimal
import java.util.Locale
import javax.inject.Inject

class FormatAndConvertPriceByLocaleUseCase @Inject constructor(
    private val localeFormattingRepository: LocaleFormattingRepository,
) {
    fun invoke(price: BigDecimal): String =
        when (localeFormattingRepository.getLocale()) {
            Locale.US ->
                localeFormattingRepository.formatPrice(price)

            Locale.FRANCE -> localeFormattingRepository.formatPrice(
                localeFormattingRepository.convertDollarToEuro(price)
            )

            else -> localeFormattingRepository.formatPrice(price)
        }
}