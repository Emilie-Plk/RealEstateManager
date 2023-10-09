package com.emplk.realestatemanager.domain.locale_formatting

import java.util.Locale
import javax.inject.Inject

class GetLocaleUseCase @Inject constructor(
    private val localeFormattingRepository: LocaleFormattingRepository
) {
    fun invoke(): Locale = localeFormattingRepository.getLocale()
}
