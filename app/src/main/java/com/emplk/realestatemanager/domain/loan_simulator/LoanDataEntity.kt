package com.emplk.realestatemanager.domain.loan_simulator

import java.math.BigDecimal

data class LoanDataEntity(
    val loanAmount: BigDecimal? = BigDecimal.ZERO,
    val interestRate: BigDecimal? = BigDecimal.ZERO,
    val loanDuration: BigDecimal? = BigDecimal.ZERO,
    val yearlyPayment: BigDecimal? = BigDecimal.ZERO,
    val monthlyPayment: BigDecimal? = BigDecimal.ZERO,
)
