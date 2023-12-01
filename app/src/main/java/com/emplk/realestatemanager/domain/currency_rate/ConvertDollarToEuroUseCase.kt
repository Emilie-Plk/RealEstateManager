package com.emplk.realestatemanager.domain.currency_rate

import com.emplk.realestatemanager.data.locale_formatting.LocaleFormattingRepositoryImpl
import java.math.BigDecimal
import javax.inject.Inject

class ConvertDollarToEuroUseCase @Inject constructor(
    private val localeFormattingRepositoryImpl: LocaleFormattingRepositoryImpl,
) {
    fun invoke(priceInUsd: BigDecimal, usdToEuroRate: BigDecimal): BigDecimal =
        localeFormattingRepositoryImpl.convertDollarToEuroRoundedHalfUp(priceInUsd, usdToEuroRate)
}