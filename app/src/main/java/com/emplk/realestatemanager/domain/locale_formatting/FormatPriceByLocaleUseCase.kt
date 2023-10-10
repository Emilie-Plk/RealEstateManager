package com.emplk.realestatemanager.domain.locale_formatting

import java.math.BigDecimal
import javax.inject.Inject

class FormatPriceByLocaleUseCase @Inject constructor(
    private val localeFormattingRepository: LocaleFormattingRepository,
) {
    fun invoke(price: BigDecimal): String = localeFormattingRepository.formatPrice(price)
}