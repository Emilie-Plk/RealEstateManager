package com.emplk.realestatemanager.domain.loan_simulator

import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class GetLoanYearlyAndMonthlyPaymentUseCase @Inject constructor() {
    fun invoke(loanAmount: BigDecimal, loanInterest: BigDecimal, loanDuration: BigDecimal): YearlyAndMonthlyPayment? {
        if (loanAmount <= BigDecimal.ZERO || loanInterest <= BigDecimal.ZERO || loanDuration <= BigDecimal.ZERO) {
            return null
        }

        val monthlyInterestRate = loanInterest.divide(BigDecimal(12 * 100), 8, RoundingMode.HALF_UP)
        val loanDurationInMonths = loanDuration.multiply(BigDecimal(12))

        val numerator =
            monthlyInterestRate.multiply((BigDecimal.ONE + monthlyInterestRate).pow(loanDurationInMonths.toInt()))
        val denominator = (BigDecimal.ONE + monthlyInterestRate).pow(loanDurationInMonths.toInt()) - BigDecimal.ONE

        val monthlyPayment: BigDecimal = loanAmount.multiply(numerator).divide(denominator, 2, RoundingMode.HALF_UP)
        val yearlyPayment: BigDecimal = monthlyPayment.multiply(BigDecimal(12))

        return YearlyAndMonthlyPayment(yearlyPayment, monthlyPayment)
    }

    data class YearlyAndMonthlyPayment(
        val yearlyPayment: BigDecimal,
        val monthlyPayment: BigDecimal,
    )
}
