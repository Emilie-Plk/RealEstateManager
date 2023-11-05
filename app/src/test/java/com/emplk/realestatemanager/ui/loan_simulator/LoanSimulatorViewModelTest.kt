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
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativeText
import com.emplk.utils.TestCoroutineRule
import com.emplk.utils.observeForTesting
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
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
        loanSimulatorViewModel.onInterestRateChanged("2.22")
        every { getLoanDataAsFlowUseCase.invoke() } returns flowOf(
            testLoanDataEntity.copy(
                interestRate = BigDecimal(2.22).setScale(2, RoundingMode.HALF_UP)
            )
        )

        // When
        loanSimulatorViewModel.viewState.observeForTesting(this) {
            // Then
            assertThat(it.value!!.loanRate).isEqualTo("2.22")
        }
    }

    @Test
    fun `on loan amount change - should update value`() = testCoroutineRule.runTest {
        // Given
        loanSimulatorViewModel.onLoanAmountChanged("2000000")
        every { getLoanDataAsFlowUseCase.invoke() } returns flowOf(
            testLoanDataEntity.copy(
                loanAmount = BigDecimal(2000000)
            )
        )

        // When
        loanSimulatorViewModel.viewState.observeForTesting(this) {
            // Then
            assertThat(it.value!!.loanAmount).isEqualTo("2000000")
        }
    }

    @Test
    fun `on duration change - should update value`() = testCoroutineRule.runTest {
        // Given
        loanSimulatorViewModel.onLoanAmountChanged("2000000")
        every { getLoanDataAsFlowUseCase.invoke() } returns flowOf(
            testLoanDataEntity.copy(
                loanAmount = BigDecimal(2000000)
            )
        )

        // When
        loanSimulatorViewModel.viewState.observeForTesting(this) {
            // Then
            assertThat(it.value!!.loanAmount).isEqualTo("2000000")
        }
    }

    @Test
    fun `on calculate clicked with missing interest rate - should trigger event`() = testCoroutineRule.runTest {
        // Given
        every { getLoanDataAsFlowUseCase.invoke() } returns flowOf(
            testLoanDataEntity.copy(
                interestRate = BigDecimal(0)
            )
        )

        loanSimulatorViewModel.viewEvent.observeForTesting(this) { event ->
            loanSimulatorViewModel.viewState.observeForTesting(this) { viewState ->
                viewState.value!!.onCalculateClicked.invoke()
                runCurrent()

                assertThat(event.value).isEqualTo(
                    Event(
                        LoanErrorMessages(
                            loanDurationErrorMessage = null,
                            amountErrorMessage = null,
                            interestRateErrorMessage = NativeText.Resource(R.string.loan_simulator_calculate_button_error_message)
                        )
                    )
                )
            }
        }
    }

    @Test
    fun `on calculate clicked with missing loan amount - should trigger event`() = testCoroutineRule.runTest {
        // Given
        every { getLoanDataAsFlowUseCase.invoke() } returns flowOf(
            testLoanDataEntity.copy(
                loanAmount = BigDecimal(0)
            )
        )

        loanSimulatorViewModel.viewEvent.observeForTesting(this) { event ->
            loanSimulatorViewModel.viewState.observeForTesting(this) { viewState ->
                viewState.value!!.onCalculateClicked.invoke()
                runCurrent()

                assertThat(event.value).isEqualTo(
                    Event(
                        LoanErrorMessages(
                            loanDurationErrorMessage = null,
                            amountErrorMessage = NativeText.Resource(R.string.loan_simulator_calculate_button_error_message),
                            interestRateErrorMessage = null
                        )
                    )
                )
            }
        }
    }

    @Test
    fun `on calculate clicked with missing duration - should trigger event`() = testCoroutineRule.runTest {
        // Given
        every { getLoanDataAsFlowUseCase.invoke() } returns flowOf(
            testLoanDataEntity.copy(
                loanDuration = BigDecimal(0)
            )
        )

        loanSimulatorViewModel.viewEvent.observeForTesting(this) { event ->
            loanSimulatorViewModel.viewState.observeForTesting(this) { viewState ->
                viewState.value!!.onCalculateClicked.invoke()
                runCurrent()

                assertThat(event.value).isEqualTo(
                    Event(
                        LoanErrorMessages(
                            loanDurationErrorMessage = NativeText.Resource(
                                R.string.loan_simulator_calculate_button_error_message
                            ), amountErrorMessage = null, interestRateErrorMessage = null
                        )
                    )
                )
            }
        }
    }

    @Test
    fun `onResumed() called - should trigger setLoanDataUseCase`() = testCoroutineRule.runTest {
        // When
        loanSimulatorViewModel.onResume()

        // Then
        loanSimulatorViewModel.viewState.observeForTesting(this) {
            assertThat(it.value).isEqualTo(testLoanSimulatorViewState)
        } // called twice bc when launching it tis empty form!
        verify(exactly = 1) { setLoanDataUseCase.invoke(any()) }
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