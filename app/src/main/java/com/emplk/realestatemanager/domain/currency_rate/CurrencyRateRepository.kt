package com.emplk.realestatemanager.domain.currency_rate

import com.emplk.realestatemanager.data.currency_rate.response.RateResponse

interface CurrencyRateRepository {
suspend fun getCurrentCurrencyRate(): CurrencyRateWrapper
}
