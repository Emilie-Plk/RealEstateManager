package com.emplk.realestatemanager.domain.currency_rate

import java.math.BigDecimal
import java.time.LocalDate

data class CurrencyRateEntity(
    val usdToEuroRate: BigDecimal,
    val lastUpdatedDate: LocalDate,
)