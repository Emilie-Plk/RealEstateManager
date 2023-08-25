package com.emplk.realestatemanager.domain.locale_formatting

interface LocaleFormattingRepository {
    fun getLocaleCountry(): String
    fun getLocaleCurrencyFormatting(): CurrencyType
    fun getLocaleSurfaceUnitFormatting(): SurfaceUnitType
}