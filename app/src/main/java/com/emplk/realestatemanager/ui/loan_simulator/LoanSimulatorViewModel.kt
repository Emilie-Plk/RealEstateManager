package com.emplk.realestatemanager.ui.loan_simulator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.loan_simulator.GetLoanDataAsFlowUseCase
import com.emplk.realestatemanager.domain.loan_simulator.GetLoanYearlyAndMonthlyPaymentUseCase
import com.emplk.realestatemanager.domain.loan_simulator.LoanParams
import com.emplk.realestatemanager.domain.loan_simulator.ResetLoanDataUseCase
import com.emplk.realestatemanager.domain.loan_simulator.SetLoanDataUseCase
import com.emplk.realestatemanager.domain.locale_formatting.currency.FormatPriceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.locale_formatting.currency.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class LoanSimulatorViewModel @Inject constructor(
    private val setLoanDataUseCase: SetLoanDataUseCase,
    private val getLoanDataAsFlowUseCase: GetLoanDataAsFlowUseCase,
    private val getLoanYearlyAndMonthlyPaymentUseCase: GetLoanYearlyAndMonthlyPaymentUseCase,
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase,
    private val formatPriceToHumanReadableUseCase: FormatPriceToHumanReadableUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val resetLoanDataUseCase: ResetLoanDataUseCase,
) : ViewModel() {

    private val loanParamsMutableStateFlow: MutableStateFlow<LoanParams> = MutableStateFlow(LoanParams())
    private val errorMessageMutableSharedFlow: MutableStateFlow<LoanErrorMessages> =
        MutableStateFlow(LoanErrorMessages())

    val viewState: LiveData<LoanSimulatorViewState> = liveData {
        coroutineScope {
            launch {
                getLoanDataAsFlowUseCase.invoke().collect { loanData ->
                    loanParamsMutableStateFlow.update {
                        it.copy(
                            loanAmount = loanData.loanAmount,
                            interestRate = loanData.interestRate,
                            loanDuration = loanData.loanDuration,
                            yearlyPayment = loanData.yearlyPayment,
                            monthlyPayment = loanData.monthlyPayment,
                        )
                    }
                    Log.d("LoanSimulatorViewModel", "LoanData collected: $loanData")
                }
            }

            setLoanDataUseCase.invoke(loanParamsMutableStateFlow.value)

            loanParamsMutableStateFlow.collectLatest { loanParams ->
                emit(
                    LoanSimulatorViewState(
                        loanAmount = if (loanParams.loanAmount == BigDecimal.ZERO) "" else loanParams.loanAmount.toString(),
                        loanRate = if (loanParams.interestRate == BigDecimal.ZERO) "" else loanParams.interestRate.toString(),
                        loanDuration = if (loanParams.loanDuration == BigDecimal.ZERO) "" else loanParams.loanDuration.toString(),
                        loanCurrencyHint = NativeText.Argument(
                            R.string.loan_amount_hint,
                            getCurrencyTypeUseCase.invoke().symbol
                        ),
                        yearlyAndMonthlyPayment = if (
                            loanParams.yearlyPayment == BigDecimal.ZERO &&
                            loanParams.monthlyPayment == BigDecimal.ZERO
                        ) null else
                            NativeText.Arguments(
                                R.string.loan_simulator_payment_result,
                                listOf(
                                    formatPriceToHumanReadableUseCase.invoke(loanParams.yearlyPayment),
                                    formatPriceToHumanReadableUseCase.invoke(loanParams.monthlyPayment),
                                )
                            ),
                        onCalculateClicked = EquatableCallback {
                            if (loanParams.loanAmount == BigDecimal.ZERO ||
                                loanParams.interestRate == BigDecimal.ZERO ||
                                loanParams.loanDuration == BigDecimal.ZERO
                            ) {
                                errorMessageMutableSharedFlow.tryEmit(
                                    LoanErrorMessages(
                                        amountErrorMessage = if (loanParams.loanAmount == BigDecimal.ZERO) NativeText.Resource(
                                            R.string.loan_simulator_calculate_button_error_message
                                        ) else null,
                                        interestRateErrorMessage = if (loanParams.interestRate == BigDecimal.ZERO) NativeText.Resource(
                                            R.string.loan_simulator_calculate_button_error_message
                                        ) else null,
                                        loanDurationErrorMessage = if (loanParams.loanDuration == BigDecimal.ZERO) NativeText.Resource(
                                            R.string.loan_simulator_calculate_button_error_message
                                        ) else null,
                                    )
                                )
                                return@EquatableCallback
                            }
                            val result = getLoanYearlyAndMonthlyPaymentUseCase.invoke(
                                loanParamsMutableStateFlow.value.loanAmount,
                                loanParamsMutableStateFlow.value.interestRate,
                                loanParamsMutableStateFlow.value.loanDuration
                            )
                            if (result != null) {
                                setLoanDataUseCase.invoke(loanParamsMutableStateFlow.value)
                                loanParamsMutableStateFlow.update {
                                    it.copy(
                                        yearlyPayment = result.yearlyPayment,
                                        monthlyPayment = result.monthlyPayment
                                    )
                                }
                            }
                        },
                        onResetClicked = EquatableCallback {
                            resetLoanDataUseCase.invoke()
                        },
                    )
                )
            }
        }
    }

    val viewEvent: LiveData<Event<LoanErrorMessages>> = liveData {
        errorMessageMutableSharedFlow.collectLatest {
            emit(Event(it))
        }
    }

    fun onLoanAmountChanged(loanAmount: String) {
        if (errorMessageMutableSharedFlow.value.amountErrorMessage != null)
            errorMessageMutableSharedFlow.tryEmit(LoanErrorMessages(amountErrorMessage = null))

        if (loanAmount.isBlank() || loanAmount.startsWith("0")) return

        loanParamsMutableStateFlow.update {
            it.copy(
                loanAmount = BigDecimal(loanAmount)
            )
        }
    }

    fun onInterestRateChanged(interestRate: String) {
        if (errorMessageMutableSharedFlow.value.interestRateErrorMessage != null)
            errorMessageMutableSharedFlow.tryEmit(LoanErrorMessages(interestRateErrorMessage = null))
        if (interestRate.isBlank() || interestRate.startsWith("0")) return

        loanParamsMutableStateFlow.update {
            it.copy(
                interestRate = BigDecimal(interestRate)
            )
        }
    }

    fun onLoanDurationChanged(loanDuration: String) {
        if (errorMessageMutableSharedFlow.value.loanDurationErrorMessage != null)
            errorMessageMutableSharedFlow.tryEmit(LoanErrorMessages(loanDurationErrorMessage = null))
        if (loanDuration.isBlank() || loanDuration.startsWith("0")) return

        loanParamsMutableStateFlow.update {
            it.copy(
                loanDuration = BigDecimal(loanDuration)
            )
        }
    }

    fun onResume() {
        setLoanDataUseCase.invoke(loanParamsMutableStateFlow.value)
    }

    fun onLoanAmountReset() {
        loanParamsMutableStateFlow.update {
            it.copy(
                interestRate = BigDecimal.ZERO
            )
        }
    }

    fun onInterestRateReset() {
        loanParamsMutableStateFlow.update {
            it.copy(
                interestRate = BigDecimal.ZERO
            )
        }
    }

    fun onLoanDurationReset() {
        loanParamsMutableStateFlow.update {
            it.copy(
                loanDuration = BigDecimal.ZERO
            )
        }
    }

    fun onStop() {
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
        resetLoanDataUseCase.invoke()
    }
}

data class LoanErrorMessages(
    val amountErrorMessage: NativeText? = null,
    val interestRateErrorMessage: NativeText? = null,
    val loanDurationErrorMessage: NativeText? = null,
)