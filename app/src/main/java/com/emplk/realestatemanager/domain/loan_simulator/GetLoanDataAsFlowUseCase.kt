package com.emplk.realestatemanager.domain.loan_simulator

import javax.inject.Inject

class GetLoanDataAsFlowUseCase @Inject constructor(
    private val loanSimulatorRepository: LoanSimulatorRepository,
) {
    fun invoke() = loanSimulatorRepository.getLoanDataAsFlow()
}