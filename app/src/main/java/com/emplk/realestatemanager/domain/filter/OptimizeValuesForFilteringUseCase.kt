package com.emplk.realestatemanager.domain.filter

import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

/**
 * Round down min value and round up max value to avoid missing properties
 * when placing the request to the database
 * @param minValue the min value of the filter (price or surface)
 * @param maxValue the max value of the filter (price or surface)
 */
class OptimizeValuesForFilteringUseCase @Inject constructor() {
    fun invoke(minValue: BigDecimal, maxValue: BigDecimal): Pair<BigDecimal, BigDecimal> {
        return if (minValue == BigDecimal.ZERO && maxValue == BigDecimal.ZERO) {
            Pair(BigDecimal.ZERO, BigDecimal.ZERO)
        } else {
            var min = BigDecimal.ZERO
            var max = BigDecimal.ZERO
            if (minValue != BigDecimal.ZERO) {
                min = minValue.subtract(BigDecimal(0.5)).setScale(2, RoundingMode.DOWN)
            }

            if (maxValue != BigDecimal.ZERO) {
                max = maxValue.add(BigDecimal(0.5)).setScale(2, RoundingMode.UP)
            }

            Pair(min, max)
        }
    }
}