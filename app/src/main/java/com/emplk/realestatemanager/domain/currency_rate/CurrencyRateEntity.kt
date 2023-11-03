package com.emplk.realestatemanager.domain.currency_rate

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class CurrencyRateEntity(
    val usdToEuroRate: BigDecimal,
    val lastUpdatedDate: LocalDateTime,
)