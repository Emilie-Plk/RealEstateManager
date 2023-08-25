package com.emplk.realestatemanager.domain.currency

interface CurrencyRepository {
    fun getLocaleCurrencyFormatting(): CurrencyType
}