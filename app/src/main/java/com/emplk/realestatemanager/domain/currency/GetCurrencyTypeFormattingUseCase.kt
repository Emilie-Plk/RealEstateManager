package com.emplk.realestatemanager.domain.currency


import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetCurrencyTypeFormattingUseCase @Inject constructor(
    private val currencyRepository: CurrencyRepository,
) {
    fun invoke(): Flow<CurrencyType> {
        return currencyRepository.getLocaleCurrencyFormatting()
    }
}