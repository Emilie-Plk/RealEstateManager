package com.emplk.realestatemanager.domain.loan_simulator

import com.emplk.realestatemanager.domain.loan_simulator.model.LoanDataEntity
import kotlinx.coroutines.flow.Flow

interface LoanSimulatorRepository {
    fun setLoanData(loanDataEntity: LoanDataEntity)
    fun getLoanDataAsFlow(): Flow<LoanDataEntity>
    fun resetLoanData()
}
