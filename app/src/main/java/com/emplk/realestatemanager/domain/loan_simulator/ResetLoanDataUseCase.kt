package com.emplk.realestatemanager.domain.loan_simulator

import javax.inject.Inject

class ResetLoanDataUseCase @Inject constructor(
    private val loanSimulatorRepository: LoanSimulatorRepository,
) {
    fun invoke() = loanSimulatorRepository.setLoanData(LoanDataEntity())
}