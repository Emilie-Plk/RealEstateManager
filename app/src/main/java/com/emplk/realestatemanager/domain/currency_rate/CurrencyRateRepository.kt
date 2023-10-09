package com.emplk.realestatemanager.domain.currency_rate

interface CurrencyRateRepository {
    suspend fun getCurrentCurrencyRate(): CurrencyRateWrapper
}
