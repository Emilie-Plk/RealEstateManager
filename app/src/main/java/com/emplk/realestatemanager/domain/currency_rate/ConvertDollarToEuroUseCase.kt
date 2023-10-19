package com.emplk.realestatemanager.domain.currency_rate

import com.emplk.realestatemanager.data.locale_formatting.UnitOfMeasurementRepositoryLocale
import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class ConvertDollarToEuroUseCase @Inject constructor(
    private val unitOfMeasurementRepositoryLocale: UnitOfMeasurementRepositoryLocale,
) {
    fun invoke(priceInUsd: BigDecimal, usdToEuroRate: BigDecimal): BigDecimal =
        unitOfMeasurementRepositoryLocale.convertDollarToEuro(priceInUsd, usdToEuroRate)
}