package com.emplk.realestatemanager.domain.loan_simulator

import kotlinx.coroutines.flow.Flow
import java.math.BigDecimal

interface LoanSimulatorRepository {
    fun setLoanData(loanDataEntity: LoanDataEntity)
    fun getLoanDataAsFlow(): Flow<LoanDataEntity>
    fun resetLoanData()
}
