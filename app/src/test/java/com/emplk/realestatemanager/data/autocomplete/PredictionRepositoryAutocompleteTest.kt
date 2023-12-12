package com.emplk.realestatemanager.data.autocomplete


import android.util.LruCache
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isInstanceOf
import com.emplk.realestatemanager.data.api.GoogleApi
import com.emplk.realestatemanager.data.autocomplete.response.AutocompleteResponse
import com.emplk.realestatemanager.data.autocomplete.response.MatchedSubstringResponse
import com.emplk.realestatemanager.data.autocomplete.response.PredictionResponse
import com.emplk.realestatemanager.data.autocomplete.response.StructuredFormattingResponse
import com.emplk.realestatemanager.data.autocomplete.response.TermResponse
import com.emplk.realestatemanager.domain.autocomplete.PredictionWrapper
import com.emplk.utils.TestCoroutineRule
import io.mockk.called
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.After
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PredictionRepositoryAutocompleteTest {

    companion object {
        private const val TEST_TYPE = "address"
        private const val TEST_INPUT = "TEST_INPUT"
        private const val TEST_STATUS_OK = "OK"
        private const val TEST_STATUS_ZERO_RESULTS = "ZERO_RESULTS"
        private const val TEST_STATUS_UNKNOWN_ERROR = "UNKNOWN_ERROR"
        private val TEST_PREDICTION_WRAPPER_SUCCESS: PredictionWrapper =
            PredictionWrapper.Success(listOf("blabla", "blabla"))
        private val TEST_PREDICTION_WRAPPER_NO_RESULT: PredictionWrapper = PredictionWrapper.NoResult
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val googleApi: GoogleApi = mockk()

    private val testPredictionLruCache: LruCache<String, PredictionWrapper> = mockk()

    private val predictionRepositoryAutocomplete = PredictionRepositoryAutocomplete(
        googleApi,
        testPredictionLruCache,
        testCoroutineRule.getTestCoroutineDispatcherProvider(),
    )

    @Before
    fun setUp() {
        every { testPredictionLruCache.maxSize() } returns 200
        every { testPredictionLruCache.get(any()) } returns null
        every { testPredictionLruCache.put(any(), any()) } returns null
        coEvery { googleApi.getAddressPredictions(TEST_INPUT, TEST_TYPE) } returns
                AutocompleteResponse(getTestPredictionsResponses(), TEST_STATUS_OK)
    }

    @After
    fun tearDown() {
        every { testPredictionLruCache.evictAll() }
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // When
        val result = predictionRepositoryAutocomplete.getAddressPredictions(TEST_INPUT)

        // Then
        assertThat(result).isEqualTo(TEST_PREDICTION_WRAPPER_SUCCESS)
        verify { testPredictionLruCache.put(TEST_INPUT, TEST_PREDICTION_WRAPPER_SUCCESS) }
        coVerify(exactly = 1) { googleApi.getAddressPredictions(TEST_INPUT, TEST_TYPE) }
        confirmVerified(googleApi)
    }

    @Test
    fun `get address predictions - with lru cache`() = testCoroutineRule.runTest {
        // Given
        every { testPredictionLruCache.get(TEST_INPUT) } returns TEST_PREDICTION_WRAPPER_SUCCESS

        // When
        val result = predictionRepositoryAutocomplete.getAddressPredictions(TEST_INPUT)

        // Then
        assertThat(result).isEqualTo(TEST_PREDICTION_WRAPPER_SUCCESS)

        verify(exactly = 1) { testPredictionLruCache.get(TEST_INPUT) }
        coVerify(exactly = 0) { googleApi.getAddressPredictions(TEST_INPUT, TEST_TYPE) }
        verify(exactly = 0) { testPredictionLruCache.put(TEST_INPUT, TEST_PREDICTION_WRAPPER_SUCCESS) }
        verify(exactly = 1) { googleApi wasNot called }
        confirmVerified(googleApi)
    }

    @Test
    fun `status Zero_Results - gives PredictionWrapper NoResult`() = testCoroutineRule.runTest {
        // Given
        coEvery { googleApi.getAddressPredictions(TEST_INPUT, TEST_TYPE) } returns
                AutocompleteResponse(getTestPredictionsResponses(), TEST_STATUS_ZERO_RESULTS)

        // When
        val result = predictionRepositoryAutocomplete.getAddressPredictions(TEST_INPUT)

        // Then
        assertThat(result).isEqualTo(TEST_PREDICTION_WRAPPER_NO_RESULT)
        coVerify(exactly = 1) { googleApi.getAddressPredictions(TEST_INPUT, TEST_TYPE) }
        confirmVerified(googleApi)
    }

    @Test
    fun `status OK with null predictions - gives PredictionWrapper NoResult`() = testCoroutineRule.runTest {
        // Given
        every { testPredictionLruCache.get(any()) } returns null
        coEvery { googleApi.getAddressPredictions(TEST_INPUT, TEST_TYPE) } returns
                AutocompleteResponse(emptyList(), TEST_STATUS_OK)

        // When
        val result = predictionRepositoryAutocomplete.getAddressPredictions(TEST_INPUT)

        // Then
        assertThat(result).isEqualTo(TEST_PREDICTION_WRAPPER_NO_RESULT)
        coVerify(exactly = 1) { googleApi.getAddressPredictions(TEST_INPUT, TEST_TYPE) }
        confirmVerified(googleApi)
    }

    @Test
    fun `googleApi call gives UNKNOWN_ERROR status - should catch & throw an Exception`() = testCoroutineRule.runTest {
        // Given
        coEvery { googleApi.getAddressPredictions(TEST_INPUT, TEST_TYPE) } returns
                AutocompleteResponse(emptyList(), TEST_STATUS_UNKNOWN_ERROR)

        // When
        val result = predictionRepositoryAutocomplete.getAddressPredictions(TEST_INPUT)

        // Then
        assertThat(result).isInstanceOf(PredictionWrapper.Failure::class)
        assertThrows(Exception::class.java) {
            coEvery {
                predictionRepositoryAutocomplete.getAddressPredictions(
                    TEST_INPUT
                )
            }
        }
        coVerify(exactly = 1) { googleApi.getAddressPredictions(TEST_INPUT, TEST_TYPE) }
        confirmVerified(googleApi)
    }


    @Test
    fun `googleApi call gives Exception - should catch & throw an Exception`() = testCoroutineRule.runTest {
        // Given
        coEvery { googleApi.getAddressPredictions(TEST_INPUT, TEST_TYPE) } returns
                AutocompleteResponse(emptyList(), TEST_STATUS_UNKNOWN_ERROR)

        // When
        val result = predictionRepositoryAutocomplete.getAddressPredictions(TEST_INPUT)

        // Then
        assertThat(result).isInstanceOf(PredictionWrapper.Failure::class)
        assertThrows(Exception::class.java) {
            coEvery {
                predictionRepositoryAutocomplete.getAddressPredictions(
                    TEST_INPUT
                )
            }
        }
        coVerify(exactly = 1) { googleApi.getAddressPredictions(TEST_INPUT, TEST_TYPE) }
        confirmVerified(googleApi)
    }

    private fun getTestPredictionsResponses() = buildList {
        add(
            PredictionResponse(
                "blabla",
                listOf(MatchedSubstringResponse(1, 2)),
                "placeId1",
                "reference1",
                StructuredFormattingResponse("mainText1", emptyList(), "bla"),
                listOf(TermResponse(1, "bla")),
                listOf("type1", "type2")
            )
        )

        add(
            PredictionResponse(
                "blabla",
                listOf(MatchedSubstringResponse(1, 2)),
                "placeId2",
                "reference2",
                StructuredFormattingResponse("mainText1", emptyList(), "bla"),
                listOf(TermResponse(1, "bla")),
                listOf("type1", "type2")
            )
        )
    }
}