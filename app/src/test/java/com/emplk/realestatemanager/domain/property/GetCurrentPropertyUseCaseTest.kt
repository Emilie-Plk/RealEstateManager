package com.emplk.realestatemanager.domain.property

import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.fixtures.getTestPropertyEntity
import com.emplk.utils.TestCoroutineRule
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class GetCurrentPropertyUseCaseTest {

    companion object {
        private const val TEST_PROPERTY_ID = 1L
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val propertyRepository: PropertyRepository = mockk()
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase = mockk()

    private val getCurrentPropertyUseCase = GetCurrentPropertyUseCase(
        propertyRepository,
        getCurrentPropertyIdFlowUseCase,
    )

    @Before
    fun setUp() {
        every { getCurrentPropertyIdFlowUseCase.invoke() } returns flowOf(TEST_PROPERTY_ID)
        every { propertyRepository.getPropertyByIdAsFlow(TEST_PROPERTY_ID) } returns flowOf(
            getTestPropertyEntity(
                TEST_PROPERTY_ID
            )
        )
    }

    @Test
    fun `invoke - nominal case`() = testCoroutineRule.runTest {
        // When
        getCurrentPropertyUseCase.invoke().test {
            val capturedPropertyEntity = awaitItem()
            assertThat(capturedPropertyEntity).isEqualTo(getTestPropertyEntity(TEST_PROPERTY_ID))
            assertThat(capturedPropertyEntity.id).isEqualTo(TEST_PROPERTY_ID)

            awaitComplete()
            ensureAllEventsConsumed()
        }
        // Then
        coVerify(exactly = 1) { getCurrentPropertyIdFlowUseCase.invoke() }
        coVerify(exactly = 1) { propertyRepository.getPropertyByIdAsFlow(TEST_PROPERTY_ID) }
        confirmVerified(getCurrentPropertyIdFlowUseCase)
        confirmVerified(propertyRepository)
    }

    @Test
    fun `invoke - property id is null`() = testCoroutineRule.runTest {
        // Given
        every { getCurrentPropertyIdFlowUseCase.invoke() } returns flowOf(null)

        getCurrentPropertyUseCase.invoke().test {
            // When
            awaitComplete()

            // Then
            coVerify(exactly = 1) { getCurrentPropertyIdFlowUseCase.invoke() }
            coVerify(exactly = 0) { propertyRepository.getPropertyByIdAsFlow(TEST_PROPERTY_ID) }
            confirmVerified(getCurrentPropertyIdFlowUseCase, propertyRepository)
        }
    }

    @Test
    fun `invoke - current property is null`() = testCoroutineRule.runTest {
        // Given
        every { propertyRepository.getPropertyByIdAsFlow(TEST_PROPERTY_ID) } returns flowOf(null)

        getCurrentPropertyUseCase.invoke().test {
            // When
            awaitComplete()

            // Then
            coVerify(exactly = 1) {
                getCurrentPropertyIdFlowUseCase.invoke()
                propertyRepository.getPropertyByIdAsFlow(TEST_PROPERTY_ID)
            }
            confirmVerified(getCurrentPropertyIdFlowUseCase, propertyRepository)
        }
    }

    @Test
    fun `invoke - current property id change should be reactive`() = testCoroutineRule.runTest {
        // Given
        every { getCurrentPropertyIdFlowUseCase.invoke() } returns flowOf(2L)
        every { propertyRepository.getPropertyByIdAsFlow(2L) } returns flowOf(getTestPropertyEntity(2L))


        // When
        getCurrentPropertyUseCase.invoke().test {
            val capturedPropertyEntity = awaitItem()
            assertThat(capturedPropertyEntity).isEqualTo(getTestPropertyEntity(2L))
            awaitComplete()
        }

        every { getCurrentPropertyIdFlowUseCase.invoke() } returns flowOf(456L)
        every { propertyRepository.getPropertyByIdAsFlow(456L) } returns flowOf(getTestPropertyEntity(456L))

        getCurrentPropertyUseCase.invoke().test {
            val capturedPropertyEntity = awaitItem()
            assertThat(capturedPropertyEntity).isEqualTo(getTestPropertyEntity(456L))
            awaitComplete()
        }

        // Then
        coVerify(exactly = 2)
        { getCurrentPropertyIdFlowUseCase.invoke() }
        coVerify(exactly = 1)
        { propertyRepository.getPropertyByIdAsFlow(2L) }
        coVerify(exactly = 1)
        { propertyRepository.getPropertyByIdAsFlow(456L) }
        confirmVerified(getCurrentPropertyIdFlowUseCase, propertyRepository)
    }
}