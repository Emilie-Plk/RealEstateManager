package com.emplk.realestatemanager.domain.loan_simulator

import java.math.BigDecimal
import java.math.RoundingMode
import javax.inject.Inject

class GetLoanYearlyAndMonthlyPaymentUseCase @Inject constructor() {
    fun invoke(loanAmount: BigDecimal, loanInterest: BigDecimal, loanDuration: BigDecimal): YearlyAndMonthlyPayment {
        val monthlyInterestRate = loanInterest.divide(BigDecimal(12), 0,  RoundingMode.HALF_UP)
        val loanDurationInMonths = loanDuration.multiply(BigDecimal(12))

        val monthlyPayment: BigDecimal = loanAmount.multiply(
            monthlyInterestRate.multiply(
                BigDecimal.ONE.add(monthlyInterestRate).pow(loanDurationInMonths.toInt())
            )
        ).divide(
            BigDecimal.ONE.add(monthlyInterestRate).pow(loanDurationInMonths.toInt()).subtract(BigDecimal.ONE),
            0,
            RoundingMode.HALF_UP
        )

        val yearlyPayment: BigDecimal = monthlyPayment.multiply(BigDecimal(12))

        return YearlyAndMonthlyPayment(yearlyPayment, monthlyPayment)
    }


    data class YearlyAndMonthlyPayment(
        val yearlyPayment: BigDecimal,
        val monthlyPayment: BigDecimal,
    )
}
