package com.emplk.realestatemanager.domain.currency_rate

import java.math.BigDecimal

sealed class CurrencyRateWrapper {
    data class Success(val currencyRateEntity: CurrencyRateEntity) : CurrencyRateWrapper()
    data class Error(val fallbackUsToEuroRate: BigDecimal) : CurrencyRateWrapper()
}
