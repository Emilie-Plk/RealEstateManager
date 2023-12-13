package com.emplk.realestatemanager.domain.locale_formatting

import java.math.BigDecimal
import javax.inject.Inject

class FormatPriceToHumanReadableUseCase @Inject constructor(
    private val localeFormattingRepository: LocaleFormattingRepository,
) {
    /**
     * @return String of the price formatted depending on locale with currency symbol (€ or $)
     */
    fun invoke(price: BigDecimal): String = localeFormattingRepository.formatPriceToHumanReadable(price)
}