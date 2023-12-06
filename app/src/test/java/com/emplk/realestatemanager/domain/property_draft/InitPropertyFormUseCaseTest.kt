package com.emplk.realestatemanager.domain.property_draft

import com.emplk.realestatemanager.fixtures.getTestFormDraftEntity
import com.emplk.realestatemanager.ui.add.FormType
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class InitPropertyFormUseCaseTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val formDraftRepository: FormDraftRepository = mockk()
    private val addPropertyFormWithDetailsUseCase: AddPropertyFormWithDetailsUseCase = mockk()

    private val initPropertyFormUseCase = InitPropertyFormUseCase(
        formDraftRepository,
        addPropertyFormWithDetailsUseCase,
    )

    @Before
    fun setUp() {
        coEvery { addPropertyFormWithDetailsUseCase.invoke(any()) } returns 1L
        coEvery { formDraftRepository.getFormDraftEntityById(any()) } returns getTestFormDraftEntity(1L)
    }

    @Test
    fun `case add new draft`() = testCoroutineRule.runTest {
        // When
        val result = initPropertyFormUseCase.invoke(null)

        // Then
        assertEquals(FormWithTypeEntity(getTestFormDraftEntity(1L), FormType.ADD), result)
        coVerify(exactly = 1) {
            addPropertyFormWithDetailsUseCase.invoke(null)
        }
        confirmVerified(addPropertyFormWithDetailsUseCase)
    }

    @Test
    fun `case add draft already exists`() = testCoroutineRule.runTest {
        // Given
        coEvery { formDraftRepository.doesDraftExist(1L) } returns true
        coEvery { formDraftRepository.doesPropertyExist(1L) } returns false

        // When
        val result = initPropertyFormUseCase.invoke(1L)

        // Then
        assertEquals(1L, result.formDraftEntity.id)
        assertEquals(FormType.ADD, result.formType)
        coVerify(exactly = 1) {
            formDraftRepository.getFormDraftEntityById(1L)
            formDraftRepository.doesDraftExist(1L)
            formDraftRepository.doesPropertyExist(1L)
        }
        confirmVerified(formDraftRepository)
    }

    @Test
    fun `case edit new draft`() = testCoroutineRule.runTest {
        // Given
        coEvery { formDraftRepository.doesDraftExist(1L) } returns false
        coEvery { formDraftRepository.doesPropertyExist(1L) } returns true

        // When
        val result = initPropertyFormUseCase.invoke(1L)

        // Then
        assertEquals(
            FormWithTypeEntity(
                getTestFormDraftEntity(1L),
                FormType.EDIT
            ), result
        )

        coVerify(exactly = 1) {
            addPropertyFormWithDetailsUseCase.invoke(1L)
            formDraftRepository.getFormDraftEntityById(1L)
            formDraftRepository.doesDraftExist(1L)
            formDraftRepository.doesPropertyExist(1L)
        }
        confirmVerified(formDraftRepository, addPropertyFormWithDetailsUseCase)
    }

    @Test
    fun `case edit draft already existing`() = testCoroutineRule.runTest {
        // Given
        coEvery { formDraftRepository.doesDraftExist(1L) } returns true
        coEvery { formDraftRepository.doesPropertyExist(1L) } returns true

        // When
        val result = initPropertyFormUseCase.invoke(1L)

        // Then
        assertEquals(1L, result.formDraftEntity.id)
        assertEquals(FormType.EDIT, result.formType)
        coVerify(exactly = 1) {
            formDraftRepository.getFormDraftEntityById(1L)
            formDraftRepository.doesDraftExist(1L)
            formDraftRepository.doesPropertyExist(1L)
        }
        confirmVerified(formDraftRepository)
    }

    @Test(expected = Exception::class)
    fun `case id is not null or 0L, and doesn't exist in database`() = testCoroutineRule.runTest {
        // Given
        coEvery { formDraftRepository.doesDraftExist(5L) } returns false
        coEvery { formDraftRepository.doesPropertyExist(5L) } returns false

        // When
        val result = initPropertyFormUseCase.invoke(5L)

        // Then
        assertThrows(Exception::class.java) {
            throw Exception("Error in InitPropertyFormUseCase with id: 5")
        }
        coVerify(exactly = 1) {
            formDraftRepository.getFormDraftEntityById(5L)
            formDraftRepository.doesDraftExist(5L)
            formDraftRepository.doesPropertyExist(5L)
        }
        confirmVerified(formDraftRepository)
    }
}