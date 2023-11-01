package com.emplk.realestatemanager.domain.loan_simulator

import javax.inject.Inject

class SetLoanDataUseCase @Inject constructor(
    private val loanSimulatorRepository: LoanSimulatorRepository,
) {
    fun invoke(loanParams: LoanParams) =
        loanSimulatorRepository.setLoanData(
            LoanDataEntity(
                loanAmount = loanParams.loanAmount,
                interestRate = loanParams.interestRate,
                loanDuration = loanParams.loanDuration,
                yearlyPayment = loanParams.yearlyPayment,
                monthlyPayment = loanParams.monthlyPayment,
            )
        )
}