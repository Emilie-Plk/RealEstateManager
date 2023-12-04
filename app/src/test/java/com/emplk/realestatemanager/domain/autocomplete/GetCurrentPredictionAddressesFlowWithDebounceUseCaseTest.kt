package com.emplk.realestatemanager.domain.autocomplete

import app.cash.turbine.test
import com.emplk.realestatemanager.data.property_draft.address.PredictionAddressState
import com.emplk.realestatemanager.domain.property_draft.address.PredictionAddressStateRepository
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.advanceTimeBy
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetCurrentPredictionAddressesFlowWithDebounceUseCaseTest {

    companion object {
        private const val CURRENT_INPUT = "1st, Dummy Str"
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val predictionAddressStateRepository: PredictionAddressStateRepository = mockk()
    private val getAddressPredictionsUseCase: GetAddressPredictionsUseCase = mockk()

    private val getCurrentPredictionAddressesFlowWithDebounceUseCase =
        GetCurrentPredictionAddressesFlowWithDebounceUseCase(
            predictionAddressStateRepository,
            getAddressPredictionsUseCase
        )

    @Before
    fun setUp() {
        coEvery { predictionAddressStateRepository.getPredictionAddressStateAsFlow() } returns flowOf(
            PredictionAddressState(
                currentInput = CURRENT_INPUT,
                isAddressPredictionSelectedByUser = false,
                hasAddressPredictionFocus = true
            )
        )
        coEvery { getAddressPredictionsUseCase.invoke(CURRENT_INPUT) } returns PredictionWrapper.Success(
            listOf(
                "1st, Dummy Street, 12345, Dummy City, Dummy Country",
                "1st, Dummy Stroot, 12345, Dummy City, Dummy Country",
                "1st, Dummy Strout, 12345, Dummy City, Dummy Country",
                "1st, Dummy Straaat, 12345, Dummy City, Dummy Country",
            )
        )
    }

    @Test
    fun `invoke() nominal case`() = testCoroutineRule.runTest {
        // When
        getCurrentPredictionAddressesFlowWithDebounceUseCase.invoke().test {
            val result = awaitItem()

            // Then
            assertTrue(result is PredictionWrapper.Success)
            assertEquals(4, (result as PredictionWrapper.Success).predictions.size)
            assertEquals(
                listOf(
                    "1st, Dummy Street, 12345, Dummy City, Dummy Country",
                    "1st, Dummy Stroot, 12345, Dummy City, Dummy Country",
                    "1st, Dummy Strout, 12345, Dummy City, Dummy Country",
                    "1st, Dummy Straaat, 12345, Dummy City, Dummy Country",
                ),
                result.predictions
            )
            cancelAndConsumeRemainingEvents()

            coVerify(exactly = 1) {
                getAddressPredictionsUseCase.invoke("1st, Dummy Str")
                predictionAddressStateRepository.getPredictionAddressStateAsFlow()
            }
            confirmVerified(getAddressPredictionsUseCase, predictionAddressStateRepository)
        }
    }

    @Test
    fun `same prediction address state collected won't retrigger invoke()`() = testCoroutineRule.runTest {
        // Given
        coEvery { predictionAddressStateRepository.getPredictionAddressStateAsFlow() } returnsMany listOf(
            flowOf(
                PredictionAddressState(
                    currentInput = "1st, Dummy Str",
                    isAddressPredictionSelectedByUser = false,
                    hasAddressPredictionFocus = true
                ),
                PredictionAddressState(
                    currentInput = "1st, Dummy Str",
                    isAddressPredictionSelectedByUser = false,
                    hasAddressPredictionFocus = true
                ),
                PredictionAddressState(
                    currentInput = "1st, Dummy Str",
                    isAddressPredictionSelectedByUser = false,
                    hasAddressPredictionFocus = true
                ),

                )
        )

        getCurrentPredictionAddressesFlowWithDebounceUseCase.invoke().test {
            // When
            awaitItem()
            cancelAndConsumeRemainingEvents()
        }

        // Then
        coVerify(exactly = 1) {
            getAddressPredictionsUseCase.invoke("1st, Dummy Str")
            predictionAddressStateRepository.getPredictionAddressStateAsFlow()
        }
        confirmVerified(getAddressPredictionsUseCase, predictionAddressStateRepository)
    }

    @Test
    fun `nothing is emitted before 400ms`() = testCoroutineRule.runTest {
        getCurrentPredictionAddressesFlowWithDebounceUseCase.invoke().test {
            // When
            advanceTimeBy(399)
            expectNoEvents()

            advanceTimeBy(1)
            assertTrue(awaitItem() is PredictionWrapper.Success)
            cancelAndConsumeRemainingEvents()

            // Then
            coVerify(exactly = 1) {
                getAddressPredictionsUseCase.invoke("1st, Dummy Str")
                predictionAddressStateRepository.getPredictionAddressStateAsFlow()
            }
            confirmVerified(getAddressPredictionsUseCase, predictionAddressStateRepository)
        }
    }


    @Test
    fun `invoke() returns null when currentInput is null`() = testCoroutineRule.runTest {
        // Given
        coEvery { predictionAddressStateRepository.getPredictionAddressStateAsFlow() } returns flowOf(
            PredictionAddressState(
                currentInput = null,
                isAddressPredictionSelectedByUser = false,
                hasAddressPredictionFocus = true
            )
        )

        // When
        getCurrentPredictionAddressesFlowWithDebounceUseCase.invoke().test {
            assertNull(awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }
}