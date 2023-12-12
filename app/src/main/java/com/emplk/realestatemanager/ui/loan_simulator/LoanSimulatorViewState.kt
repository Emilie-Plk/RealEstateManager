package com.emplk.realestatemanager.ui.loan_simulator

import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativeText
import java.lang.annotation.Native

data class LoanSimulatorViewState(
    val loanAmount: String,
    val loanRate: String,
    val loanDuration: String,
    val loanCurrencyHint: NativeText,
    val yearlyAndMonthlyPayment: NativeText?,
    val onCalculateClicked: EquatableCallback,
    val onResetClicked: EquatableCallback,
)
