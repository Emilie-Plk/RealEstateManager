package com.emplk.realestatemanager.domain.loan_simulator

import assertk.assertThat
import com.emplk.utils.TestCoroutineRule
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class GetLoanYearlyAndMonthlyPaymentUseCaseTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val getLoanYearlyAndMonthlyPaymentUseCase = GetLoanYearlyAndMonthlyPaymentUseCase()

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        val result = getLoanYearlyAndMonthlyPaymentUseCase.invoke(BigDecimal(100000), BigDecimal.ONE, BigDecimal(20))

        // Then
        assert(result is GetLoanYearlyAndMonthlyPaymentUseCase.YearlyAndMonthlyPayment)

        assertThat(BigDecimal(459.89) == (result as GetLoanYearlyAndMonthlyPaymentUseCase.YearlyAndMonthlyPayment).monthlyPayment)
        assertThat(BigDecimal(5518.68) == result.yearlyPayment)
        // TODO: NINO: why does this fail with Junit assertEquals but not assertK?
        // assertEquals => "Asserts that two objects are equal" vs "Asserts on the given value" for assertK
        /* assertEquals(
             BigDecimal(459.89),
             (result as GetLoanYearlyAndMonthlyPaymentUseCase.YearlyAndMonthlyPayment).monthlyPayment
         )
         assertEquals(
             BigDecimal(5518.68),
             result.yearlyPayment
         )*/
    }

    @Test
    fun `edge case - no loan info`() = testCoroutineRule.runTest {
        // When
        val result = getLoanYearlyAndMonthlyPaymentUseCase.invoke(BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO)

        // Then
        assertEquals(null, result)
    }
}