package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.domain.property_draft.model.FormDraftEntity
import com.emplk.realestatemanager.fixtures.getTestFormDraftEntity
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class AddPropertyFormWithDetailsUseCaseTestLoaded {
    companion object {
        private const val TEST_PROPERTY_ID = 1L
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val formDraftRepository: FormDraftRepository = mockk()
    private val mapPropertyToDraftUseCase: MapPropertyToDraftUseCase = mockk()

    private val addPropertyFormWithDetailsUseCase = AddPropertyFormWithDetailsUseCase(
        formDraftRepository,
        mapPropertyToDraftUseCase,
    )

    @Before
    fun setUp() {
        coEvery { formDraftRepository.addFormWithDetails(any()) } returns TEST_PROPERTY_ID
        coEvery { mapPropertyToDraftUseCase.invoke(any()) } returns testEmptyFormDraftEntity
    }

    @Test
    fun `invoke() calls addFormDraftWithDetails() with expected parameters`() = testCoroutineRule.runTest {
        // Given
        coEvery { formDraftRepository.addFormWithDetails(any()) } returns TEST_PROPERTY_ID

        // When
        val result = addPropertyFormWithDetailsUseCase.invoke(TEST_PROPERTY_ID)

        // Then
        assertEquals(TEST_PROPERTY_ID, result)
        coVerify(exactly = 1) {
            formDraftRepository.addFormWithDetails(any())
            mapPropertyToDraftUseCase.invoke(TEST_PROPERTY_ID)
        }
        confirmVerified(formDraftRepository, mapPropertyToDraftUseCase)
    }

    @Test
    fun `invoke() with id 0L calls addFormDraftWithDetails() with expected parameters`() =
        testCoroutineRule.runTest {
            // Given
            coEvery { formDraftRepository.addFormWithDetails(any()) } returns TEST_PROPERTY_ID
            coEvery { mapPropertyToDraftUseCase.invoke(0L) } returns getTestFormDraftEntity(0L)

            // When
            val result = addPropertyFormWithDetailsUseCase.invoke(0L)

            // Then
            assertEquals(TEST_PROPERTY_ID, result)
            coVerify(exactly = 1) {
                formDraftRepository.addFormWithDetails(testEmptyFormDraftEntity)
            }

            confirmVerified(formDraftRepository, mapPropertyToDraftUseCase)
        }

    @Test
    fun `invoke() with id null calls addFormDraftWithDetails() with expected parameters`() =
        testCoroutineRule.runTest {
            // Given
            coEvery { formDraftRepository.addFormWithDetails(any()) } returns TEST_PROPERTY_ID
            coEvery { mapPropertyToDraftUseCase.invoke(0L) } returns getTestFormDraftEntity(0L)

            // When
            val result = addPropertyFormWithDetailsUseCase.invoke(null)

            // Then
            assertEquals(TEST_PROPERTY_ID, result)
            coVerify(exactly = 1) {
                formDraftRepository.addFormWithDetails(testEmptyFormDraftEntity)
            }

            confirmVerified(formDraftRepository, mapPropertyToDraftUseCase)
        }
}

private val testEmptyFormDraftEntity = FormDraftEntity(
    id = 0L,
    type = null,
    title = null,
    price = BigDecimal.ZERO,
    surface = BigDecimal.ZERO,
    rooms = 0,
    bedrooms = 0,
    bathrooms = 0,
    description = null,
    amenities = emptyList(),
    address = null,
    isAddressValid = false,
    agentName = null,
    entryDate = null,
    saleDate = null,
    lastEditionDate = null,
)
