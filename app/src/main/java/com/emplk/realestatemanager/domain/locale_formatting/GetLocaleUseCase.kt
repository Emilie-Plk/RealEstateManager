package com.emplk.realestatemanager.domain.locale_formatting

import java.util.Locale
import javax.inject.Inject

class GetLocaleUseCase @Inject constructor(
    private val humanReadableRepository: HumanReadableRepository
) {
    fun invoke(): Locale = humanReadableRepository.getLocale()
}
