package com.emplk.realestatemanager.domain.currency_rate

import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class ConvertDollarToEuroUseCase @Inject constructor() {
    fun invoke(priceInUsd: BigDecimal, usdToEuroRate: BigDecimal): BigDecimal =
        priceInUsd.multiply(usdToEuroRate).setScale(0, RoundingMode.HALF_UP)
}