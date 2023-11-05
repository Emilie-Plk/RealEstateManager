package com.emplk.realestatemanager.ui.loan_simulator

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNull
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.loan_simulator.GetLoanDataAsFlowUseCase
import com.emplk.realestatemanager.domain.loan_simulator.GetLoanYearlyAndMonthlyPaymentUseCase
import com.emplk.realestatemanager.domain.loan_simulator.LoanDataEntity
import com.emplk.realestatemanager.domain.loan_simulator.ResetLoanDataUseCase
import com.emplk.realestatemanager.domain.loan_simulator.SetLoanDataUseCase
import com.emplk.realestatemanager.domain.locale_formatting.FormatPriceByLocaleUseCase
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativeText
import com.emplk.utils.TestCoroutineRule
import com.emplk.utils.observeForTesting
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.math.RoundingMode

class LoanSimulatorViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val setLoanDataUseCase: SetLoanDataUseCase = mockk()
    private val getLoanDataAsFlowUseCase: GetLoanDataAsFlowUseCase = mockk()
    private val getLoanYearlyAndMonthlyPaymentUseCase: GetLoanYearlyAndMonthlyPaymentUseCase = mockk()
    private val formatPriceByLocaleUseCase: FormatPriceByLocaleUseCase = mockk()
    private val resetLoanDataUseCase: ResetLoanDataUseCase = mockk()

    private lateinit var loanSimulatorViewModel: LoanSimulatorViewModel

    @Before
    fun setUp() {
        justRun { setLoanDataUseCase.invoke(any()) }
        justRun { resetLoanDataUseCase.invoke() }
        every { getLoanDataAsFlowUseCase.invoke() } returns flowOf(testLoanDataEntity)
        every { formatPriceByLocaleUseCase.invoke(BigDecimal(600000)) } returns "$600,000"
        every { formatPriceByLocaleUseCase.invoke(BigDecimal(5000)) } returns "$5,000"
        every {
            getLoanYearlyAndMonthlyPaymentUseCase.invoke(
                loanAmount = BigDecimal(1000000),
                loanInterest = BigDecimal(3.85).setScale(2, RoundingMode.HALF_UP), // limit to 2 decimals
                loanDuration = BigDecimal(25)
            )
        } returns GetLoanYearlyAndMonthlyPaymentUseCase.YearlyAndMonthlyPayment(
            yearlyPayment = BigDecimal(600000),
            monthlyPayment = BigDecimal(5000),
        )

        loanSimulatorViewModel = LoanSimulatorViewModel(
            setLoanDataUseCase = setLoanDataUseCase,
            getLoanDataAsFlowUseCase = getLoanDataAsFlowUseCase,
            getLoanYearlyAndMonthlyPaymentUseCase = getLoanYearlyAndMonthlyPaymentUseCase,
            formatPriceByLocaleUseCase = formatPriceByLocaleUseCase,
            resetLoanDataUseCase = resetLoanDataUseCase,
        )
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        loanSimulatorViewModel.viewState.observeForTesting(this) {
            // Then
            assertThat(it.value).isEqualTo(testLoanSimulatorViewState)
        }
    }

    @Test
    fun `missing loan params field - yearlyAndMonthlyPayment should be null`() = testCoroutineRule.runTest {
        // Given
        every { getLoanDataAsFlowUseCase.invoke() } returns flowOf(
            testLoanDataEntity.copy(
                interestRate = BigDecimal(0),
                yearlyPayment = BigDecimal(0),
                monthlyPayment = BigDecimal(0),
            )
        )
        // When
        loanSimulatorViewModel.viewState.observeForTesting(this) {
            // Then
            assertThat(it.value!!.yearlyAndMonthlyPayment).isNull()
        }
    }

    @Test
    fun `on interest rate change - should update value`() = testCoroutineRule.runTest {
        // Given

        loanSimulatorViewModel.onLoanDurationChanged("15")
        // When
        loanSimulatorViewModel.viewState.observeForTesting(this) {
            // Then
            assertThat(it.value!!.loanDuration).isEqualTo("15")
        }
    }

    private val testLoanDataEntity = LoanDataEntity(
        loanAmount = BigDecimal(1000000),
        interestRate = BigDecimal(3.85).setScale(2, RoundingMode.HALF_UP),
        loanDuration = BigDecimal(25),
        yearlyPayment = BigDecimal(600000),
        monthlyPayment = BigDecimal(5000),
    )

    private val testLoanSimulatorViewState = LoanSimulatorViewState(
        loanAmount = "1000000",
        loanRate = "3.85",
        loanDuration = "25",
        yearlyAndMonthlyPayment = NativeText.Arguments(
            R.string.loan_simulator_payment_result,
            listOf("$600,000", "$5,000")
        ),
        onCalculateClicked = EquatableCallback { },
        onResetClicked = EquatableCallback { },
    )
}