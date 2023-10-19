package com.emplk.realestatemanager.domain.currency_rate

import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class ConvertEuroToDollarUseCase @Inject constructor() {
    fun invoke(priceInEuro: BigDecimal, usdToEuroRate: BigDecimal): BigDecimal =
        priceInEuro.divide(usdToEuroRate, 0, RoundingMode.HALF_UP)
}
