package com.emplk.realestatemanager.domain.loan_simulator

import com.emplk.realestatemanager.domain.loan_simulator.model.LoanDataEntity
import com.emplk.realestatemanager.domain.loan_simulator.model.LoanParamsEntity
import javax.inject.Inject

class SetLoanDataUseCase @Inject constructor(
    private val loanSimulatorRepository: LoanSimulatorRepository,
) {
    fun invoke(loanParamsEntity: LoanParamsEntity) =
        loanSimulatorRepository.setLoanData(
            LoanDataEntity(
                loanAmount = loanParamsEntity.loanAmount,
                interestRate = loanParamsEntity.interestRate,
                loanDuration = loanParamsEntity.loanDuration,
                yearlyPayment = loanParamsEntity.yearlyPayment,
                monthlyPayment = loanParamsEntity.monthlyPayment,
            )
        )
}