package com.emplk.realestatemanager.domain.currency


import javax.inject.Inject

class GetCurrencyTypeFormattingUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository,
) {
    fun invoke(): CurrencyType = currencyRepository.getLocaleCurrencyFormatting()
}