package com.emplk.realestatemanager.domain.currency_rate

import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class ConvertDollarToEuroUseCase @Inject constructor() {
    fun invoke(priceInUsd: BigDecimal, usdToEuroRate: Double): BigDecimal =
       priceInUsd.multiply(BigDecimal(usdToEuroRate)).setScale(2, RoundingMode.HALF_UP)
}