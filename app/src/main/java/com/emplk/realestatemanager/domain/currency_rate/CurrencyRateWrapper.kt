package com.emplk.realestatemanager.domain.currency_rate

sealed class CurrencyRateWrapper {
    data class Success(val currencyRateEntity: CurrencyRateEntity) : CurrencyRateWrapper()
    data class Error(val fallbackCurrency: Double) : CurrencyRateWrapper()
}
