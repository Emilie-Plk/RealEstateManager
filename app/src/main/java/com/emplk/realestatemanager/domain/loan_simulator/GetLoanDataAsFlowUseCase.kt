package com.emplk.realestatemanager.domain.loan_simulator

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetLoanDataAsFlowUseCase @Inject constructor(
    private val loanSimulatorRepository: LoanSimulatorRepository,
) {
    fun invoke(): Flow<LoanDataEntity> = loanSimulatorRepository.getLoanDataAsFlow()
}