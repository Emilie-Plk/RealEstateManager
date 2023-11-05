package com.emplk.realestatemanager.data.loan_simulator

import com.emplk.realestatemanager.domain.loan_simulator.LoanDataEntity
import com.emplk.realestatemanager.domain.loan_simulator.LoanSimulatorRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class LoanSimulatorRepositoryImpl @Inject constructor() : LoanSimulatorRepository {
    private val loanDataMutableStateFlow = MutableStateFlow(LoanDataEntity())

    override fun setLoanData(loanDataEntity: LoanDataEntity) {
        loanDataMutableStateFlow.tryEmit(loanDataEntity)
    }

    override fun getLoanDataAsFlow(): Flow<LoanDataEntity> = loanDataMutableStateFlow

    override fun resetLoanData() {
        loanDataMutableStateFlow.tryEmit(LoanDataEntity())
    }
}