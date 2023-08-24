package com.emplk.realestatemanager.domain.currency

import kotlinx.coroutines.flow.Flow

interface CurrencyRepository {
    fun getLocaleCurrencyFormatting(): Flow<CurrencyType>
}