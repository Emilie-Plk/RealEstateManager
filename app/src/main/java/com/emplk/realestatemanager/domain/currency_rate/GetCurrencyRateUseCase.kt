package com.emplk.realestatemanager.domain.currency_rate

import javax.inject.Inject

class GetCurrencyRateUseCase @Inject constructor(
    private val currencyRateRepository: CurrencyRateRepository
) {
    suspend fun invoke(): CurrencyRateWrapper = currencyRateRepository.getCurrentCurrencyRate()
}