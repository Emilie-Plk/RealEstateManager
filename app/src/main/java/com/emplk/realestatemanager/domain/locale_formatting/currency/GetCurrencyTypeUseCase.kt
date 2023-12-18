package com.emplk.realestatemanager.domain.locale_formatting.currency


import com.emplk.realestatemanager.domain.locale_formatting.HumanReadableRepository
import javax.inject.Inject

class GetCurrencyTypeUseCase @Inject constructor(
    private val humanReadableRepository: HumanReadableRepository,
) {
    fun invoke(): CurrencyType = humanReadableRepository.getLocaleCurrencyFormatting()
}