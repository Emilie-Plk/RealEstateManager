package com.emplk.realestatemanager.domain.autocomplete

import assertk.assertThat
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetAddressPredictionsUseCaseTest {

    companion object {
        private const val TEST_RESULT_LYON = "Rue de la Paix, Lyon, France"
        private const val TEST_RESULT_PARIS = "Rue de la Paix, Paris, France"
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val predictionRepository: PredictionRepository = mockk()

    private val getAddressPredictionsUseCase = GetAddressPredictionsUseCase(predictionRepository)

    @Before
    fun setUp() {
        coEvery { predictionRepository.getAddressPredictions(any()) } returns getTestSuccessResultPredictionWrapper()
    }

    @Test
    fun `invoke - nominal case`() = testCoroutineRule.runTest {
        // When
        val result = getAddressPredictionsUseCase.invoke("Rue")

        // Then
        assert(result is PredictionWrapper.Success)
        assert((result as PredictionWrapper.Success).predictions.size == 2)
        assertThat { result.predictions[0] == TEST_RESULT_LYON }
        assertThat { result.predictions[1] == TEST_RESULT_PARIS }
        coVerify(exactly = 1) { predictionRepository.getAddressPredictions(any()) }
        confirmVerified(predictionRepository)
    }

    @Test
    fun `invoke - error case`() = testCoroutineRule.runTest {
        // Given
        coEvery { predictionRepository.getAddressPredictions(any()) } returns getTestErrorResultPredictionWrapper()

        // When
        val result = getAddressPredictionsUseCase.invoke("Rue")

        // Then
        assert(result is PredictionWrapper.Error)
        assert((result as PredictionWrapper.Error).error == "Error")
        coVerify(exactly = 1) { predictionRepository.getAddressPredictions(any()) }
        confirmVerified(predictionRepository)
    }

    @Test
    fun `invoke - no result case`() = testCoroutineRule.runTest {
        // Given
        coEvery { predictionRepository.getAddressPredictions(any()) } returns getTestNoResultPredictionWrapper()

        // When
        val result = getAddressPredictionsUseCase.invoke("Rue")

        // Then
        assert(result is PredictionWrapper.NoResult)
        coVerify(exactly = 1) { predictionRepository.getAddressPredictions(any()) }
        confirmVerified(predictionRepository)
    }

    private fun getTestNoResultPredictionWrapper() = PredictionWrapper.NoResult
    private fun getTestSuccessResultPredictionWrapper() =
        PredictionWrapper.Success(listOf(TEST_RESULT_LYON, TEST_RESULT_PARIS))

    private fun getTestErrorResultPredictionWrapper() = PredictionWrapper.Error("Error")
}