package com.emplk.realestatemanager.data.loan_simulator

import com.emplk.realestatemanager.domain.loan_simulator.LoanDataEntity
import com.emplk.realestatemanager.domain.loan_simulator.LoanSimulatorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal
import javax.inject.Inject

class LoanSimulatorRepositoryImpl @Inject constructor() : LoanSimulatorRepository {
    private val loanDataMutableStateFlow = MutableStateFlow(LoanDataEntity())

    override fun setLoanData(loanDataEntity: LoanDataEntity) {
        loanDataMutableStateFlow.tryEmit(loanDataEntity)
    }

    override fun getLoanData(): Flow<LoanDataEntity> = loanDataMutableStateFlow

    override fun getYearlyPayment(): Flow<BigDecimal> {
        return loanDataMutableStateFlow.map { loanDataEntity ->
            val loanAmount = loanDataEntity.loanAmount ?: BigDecimal.ZERO
            val loanDuration = loanDataEntity.loanDuration ?: BigDecimal.ZERO
            val interestRate = loanDataEntity.interestRate ?: BigDecimal.ZERO

            if (loanAmount != BigDecimal.ZERO && loanDuration != BigDecimal.ZERO && interestRate != BigDecimal.ZERO) {
                val monthlyInterestRate = interestRate / BigDecimal(100) / BigDecimal(12)
                val months = loanDuration.toInt() * 12

                val monthlyPayment = loanAmount * monthlyInterestRate /
                        (BigDecimal.ONE - BigDecimal.ONE / (BigDecimal.ONE + monthlyInterestRate).pow(months))

                monthlyPayment * BigDecimal(12)
            } else {
                BigDecimal.ZERO
            }
        }
    }
}