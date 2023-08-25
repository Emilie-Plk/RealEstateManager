package com.emplk.realestatemanager.domain.locale_formatting


import javax.inject.Inject

class GetCurrencyTypeUseCase @Inject constructor(
    private val localeFormattingRepository: LocaleFormattingRepository,
) {
    fun invoke(): CurrencyType = localeFormattingRepository.getLocaleCurrencyFormatting()
}