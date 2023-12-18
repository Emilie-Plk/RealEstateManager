package com.emplk.realestatemanager.domain.locale_formatting.currency

import com.emplk.realestatemanager.domain.locale_formatting.HumanReadableRepository
import java.math.BigDecimal
import javax.inject.Inject

class FormatPriceToHumanReadableUseCase @Inject constructor(
    private val humanReadableRepository: HumanReadableRepository,
) {
    /**
     * @return String of the price formatted depending on locale with currency symbol (€ or $)
     */
    fun invoke(price: BigDecimal): String = humanReadableRepository.formatRoundedPriceToHumanReadable(price)
}