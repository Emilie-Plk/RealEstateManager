package com.emplk.realestatemanager.domain.currency_rate.model

import java.math.BigDecimal
import java.time.LocalDateTime

data class CurrencyRateEntity(
    val usdToEuroRate: BigDecimal,
    val lastUpdatedDate: LocalDateTime,
)