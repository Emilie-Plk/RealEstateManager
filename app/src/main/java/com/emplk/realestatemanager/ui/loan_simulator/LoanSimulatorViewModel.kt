package com.emplk.realestatemanager.ui.loan_simulator

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.loan_simulator.GetLoanDataAsFlowUseCase
import com.emplk.realestatemanager.domain.loan_simulator.GetLoanYearlyAndMonthlyPaymentUseCase
import com.emplk.realestatemanager.domain.loan_simulator.LoanParams
import com.emplk.realestatemanager.domain.loan_simulator.ResetLoanDataUseCase
import com.emplk.realestatemanager.domain.loan_simulator.SetLoanDataUseCase
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class LoanSimulatorViewModel @Inject constructor(
    private val setLoanDataUseCase: SetLoanDataUseCase,
    private val getLoanDataAsFlowUseCase: GetLoanDataAsFlowUseCase,
    private val getLoanYearlyAndMonthlyPaymentUseCase: GetLoanYearlyAndMonthlyPaymentUseCase,
    private val resetLoanDataUseCase: ResetLoanDataUseCase,

    ) : ViewModel() {

    private val loanParamsMutableStateFlow: MutableStateFlow<LoanParams> = MutableStateFlow(LoanParams())

    val viewState: LiveData<LoanSimulatorViewState> = liveData {
        coroutineScope {
            getLoanDataAsFlowUseCase.invoke().collectLatest {
                loanParamsMutableStateFlow.update {
                    it.copy(
                        loanAmount = it.loanAmount,
                        interestRate = it.interestRate,
                        loanDuration = it.loanDuration,
                        yearlyPayment = it.yearlyPayment,
                        monthlyPayment = it.monthlyPayment,
                    )
                }
            }

            launch {
                loanParamsMutableStateFlow.transform {
                    emit(it)
                    delay(5.seconds)
                }.collect {
                    setLoanDataUseCase.invoke(it)
                }
            }
        }

        loanParamsMutableStateFlow.collect { loanParams ->
            emit(
                LoanSimulatorViewState(
                    loanAmount = if (loanParams.loanAmount == BigDecimal.ZERO) "" else loanParams.loanAmount.toString(),
                    loanRate = if (loanParams.interestRate == BigDecimal.ZERO) "" else loanParams.interestRate.toString(),
                    loanDuration = if (loanParams.loanDuration == BigDecimal.ZERO) "" else loanParams.loanDuration.toString(),
                    yearlyAndMonthlyPayment = if (
                        loanParams.loanAmount == BigDecimal.ZERO ||
                        loanParams.interestRate == BigDecimal.ZERO ||
                        loanParams.loanDuration == BigDecimal.ZERO
                    ) null else
                        NativeText.Arguments(
                            R.string.loan_simulator_payment_result,
                            listOf(
                                loanParams.yearlyPayment,
                                loanParams.monthlyPayment,
                            )
                        ),
                )
            )
        }
    }

    fun onLoanAmountChanged(loanAmount: String) {
        if (loanAmount.isBlank() || loanAmount.startsWith("0")) return
        else
            loanParamsMutableStateFlow.update {
                it.copy(
                    loanAmount = BigDecimal(loanAmount)
                )
            }
    }

    fun onInterestRateChanged(interestRate: String) {
        if (interestRate.isBlank() || interestRate.startsWith("0")) return
        else
            loanParamsMutableStateFlow.update {
                it.copy(
                    interestRate = BigDecimal(interestRate)
                )
            }
    }

    fun onLoanDurationChanged(loanDuration: String) {
        if (loanDuration.isBlank() || loanDuration.startsWith("0")) return
        else
            loanParamsMutableStateFlow.update {
                it.copy(
                    loanDuration = BigDecimal(loanDuration)
                )
            }
        setLoanDataUseCase.invoke(loanParamsMutableStateFlow.value)
    }

    fun onResetClicked() {
        loanParamsMutableStateFlow.tryEmit(LoanParams())
        resetLoanDataUseCase.invoke()
    }

    fun onCalculateClicked() {
        setLoanDataUseCase.invoke(loanParamsMutableStateFlow.value)

        val result = getLoanYearlyAndMonthlyPaymentUseCase.invoke(
            loanParamsMutableStateFlow.value.loanAmount,
            loanParamsMutableStateFlow.value.interestRate,
            loanParamsMutableStateFlow.value.loanDuration
        )
        loanParamsMutableStateFlow.update {
            it.copy(
                yearlyPayment = result.yearlyPayment,
                monthlyPayment = result.monthlyPayment
            )
        }
    }
}