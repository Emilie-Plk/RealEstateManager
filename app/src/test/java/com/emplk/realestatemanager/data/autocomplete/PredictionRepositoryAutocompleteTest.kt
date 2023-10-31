package com.emplk.realestatemanager.data.autocomplete


import android.util.LruCache
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.emplk.realestatemanager.data.api.GoogleApi
import com.emplk.realestatemanager.data.autocomplete.response.AutocompleteResponse
import com.emplk.realestatemanager.data.autocomplete.response.PredictionResponse
import com.emplk.realestatemanager.domain.autocomplete.PredictionWrapper
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.delay
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.time.Duration.Companion.milliseconds

class PredictionRepositoryAutocompleteTest {

    companion object {
        private const val TEST_TYPE = "TEST_TYPE"
        private const val TEST_INPUT = "TEST_INPUT"
        private const val TEST_STATUS_OK = "OK"
        private val TEST_LIST_PREDICTIONS : List<PredictionResponse> = emptyList()
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val googleApi: GoogleApi = mockk()

    private val testPredictionLruCache = mockk<LruCache<String, PredictionWrapper>>()

    private val predictionRepositoryAutocomplete = PredictionRepositoryAutocomplete(
        googleApi,
        testPredictionLruCache,
        testCoroutineRule.getTestCoroutineDispatcherProvider(),
    )

    @Before
    fun setUp() {
        coEvery { googleApi.getAddressPredictions(TEST_INPUT, TEST_TYPE) } coAnswers {
            AutocompleteResponse(TEST_LIST_PREDICTIONS, TEST_STATUS_OK)
        }

        every { testPredictionLruCache.get(TEST_INPUT) } returns null
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // Given
        val testPredictionWrapper = PredictionWrapper.Success(emptyList())

        // When
        val result = predictionRepositoryAutocomplete.getAddressPredictions(TEST_INPUT)

        // Then
        assertThat(result).isEqualTo(testPredictionWrapper)
        coVerify(exactly = 1) { googleApi.getAddressPredictions(TEST_INPUT, TEST_TYPE) }
        confirmVerified(googleApi)
    }
}