package com.emplk.realestatemanager.domain.currency_rate

import java.time.LocalDate

data class CurrencyRateEntity(
  val usdToEuroRate: Double,
  val lastUpdatedDate: LocalDate,
)