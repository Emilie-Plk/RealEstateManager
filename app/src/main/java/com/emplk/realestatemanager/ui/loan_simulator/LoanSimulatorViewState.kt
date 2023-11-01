package com.emplk.realestatemanager.ui.loan_simulator

import com.emplk.realestatemanager.ui.utils.NativeText

data class LoanSimulatorViewState(
    val loanAmount: String,
    val loanRate: String,
    val loanDuration: String,
    val yearlyAndMonthlyPayment: NativeText?,
    )
