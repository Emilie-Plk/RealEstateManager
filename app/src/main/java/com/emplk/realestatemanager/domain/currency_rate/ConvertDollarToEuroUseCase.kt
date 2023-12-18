package com.emplk.realestatemanager.domain.currency_rate

import com.emplk.realestatemanager.data.locale_formatting.HumanReadableRepositoryImpl
import java.math.BigDecimal
import javax.inject.Inject

class ConvertDollarToEuroUseCase @Inject constructor(
    private val humanReadableRepositoryImpl: HumanReadableRepositoryImpl,
) {
    fun invoke(priceInUsd: BigDecimal, usdToEuroRate: BigDecimal): BigDecimal =
        humanReadableRepositoryImpl.convertDollarToEuroRoundedHalfUp(priceInUsd, usdToEuroRate)
}