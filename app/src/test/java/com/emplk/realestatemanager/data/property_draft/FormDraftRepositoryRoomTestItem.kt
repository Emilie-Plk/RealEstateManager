package com.emplk.realestatemanager.data.property_draft

import android.database.sqlite.SQLiteException
import com.emplk.realestatemanager.data.property_draft.mappers.FormDraftMapper
import com.emplk.realestatemanager.data.property_draft.mappers.FormWithTitleDateAndFeaturedPictureMapper
import com.emplk.realestatemanager.data.property_draft.mappers.PicturePreviewMapper
import com.emplk.realestatemanager.data.property_draft.picture_preview.PicturePreviewDao
import com.emplk.realestatemanager.data.property_draft.picture_preview.PicturePreviewDto
import com.emplk.realestatemanager.domain.property_draft.model.FormWithDetailEntity
import com.emplk.realestatemanager.fixtures.getTestFormDraftDto
import com.emplk.realestatemanager.fixtures.getTestFormDraftEntity
import com.emplk.realestatemanager.fixtures.getTestPicturePreviewEntities
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.time.LocalDateTime

class FormDraftRepositoryRoomTestItem {

    companion object {
        private const val TEST_PROPERTY_ID = 1L
        private val TEST_FORM_DRAFT_ENTITY = getTestFormDraftEntity(TEST_PROPERTY_ID)
        private val TEST_FORM_DRAFT_WITH_DETAILS = getFormDraftWithDetails(TEST_PROPERTY_ID)
        private val TEST_FORM_DRAFT_DTO = getTestFormDraftDto(TEST_PROPERTY_ID)
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val formDraftDao: FormDraftDao = mockk()
    private val picturePreviewDao: PicturePreviewDao = mockk()
    private val formDraftMapper: FormDraftMapper = mockk()
    private val picturePreviewMapper: PicturePreviewMapper = mockk()
    private val formWithTitleDateAndFeaturedPictureMapper: FormWithTitleDateAndFeaturedPictureMapper = mockk()

    private val repository = FormDraftRepositoryRoom(
        formDraftDao,
        picturePreviewDao,
        formDraftMapper,
        picturePreviewMapper,
        formWithTitleDateAndFeaturedPictureMapper,
        testCoroutineRule.getTestCoroutineDispatcherProvider()
    )

    @Before
    fun setup() {
        coEvery { formDraftDao.insert(any()) } returns 1L
        coEvery { picturePreviewDao.insert(any()) } returns 1L
        every { formDraftMapper.mapToFormDraftDto(any()) } returns getTestFormDraftDto(TEST_PROPERTY_ID)
        every { picturePreviewMapper.mapToPicturePreviewDto(any(), any()) } returns mockk()
    }

    @Test
    fun `add() nominal case`() = testCoroutineRule.runTest {
        // Given
        val testFormDraftEntity = getTestFormDraftEntity(TEST_PROPERTY_ID)

        // When
        val result = repository.add(testFormDraftEntity)

        // Then
        assertEquals(1L, result)
        coVerify(exactly = 1) { formDraftDao.insert(any()) }
        verify(exactly = 1) { formDraftMapper.mapToFormDraftDto(testFormDraftEntity) }
        confirmVerified(formDraftDao, formDraftMapper)
    }

    @Test
    fun `addFormDraftWithDetails() nominal case`() = testCoroutineRule.runTest {
        // Given
        val testFormDraftEntity = getTestFormDraftEntity(TEST_PROPERTY_ID)

        // When
        val result = repository.addFormWithDetails(testFormDraftEntity)

        // Then
        assertEquals(1L, result)
        coVerify(exactly = 1) { formDraftDao.insert(any()) }
        verify(exactly = 1) { formDraftMapper.mapToFormDraftDto(testFormDraftEntity) }
        coVerify(exactly = 3) { picturePreviewDao.insert(any()) }
        verify(exactly = 3) { picturePreviewMapper.mapToPicturePreviewDto(any(), any()) }
        confirmVerified(formDraftDao, picturePreviewDao, formDraftMapper, picturePreviewMapper)
    }

    @Test
    fun `doesDraftExist() nominal case`() = testCoroutineRule.runTest {
        // Given
        val testPropertyId = 1L
        coEvery { formDraftDao.doesDraftExist(testPropertyId) } returns true

        // When
        val result = repository.doesFormExist(testPropertyId)

        // Then
        assertEquals(true, result)
        coVerify(exactly = 1) { formDraftDao.doesDraftExist(testPropertyId) }
        confirmVerified(formDraftDao)
    }

    @Test
    fun `doesDraftExist() with null id returns false`() = testCoroutineRule.runTest {
        // Given
        val testPropertyId: Long? = null

        // When
        val result = repository.doesFormExist(testPropertyId)

        // Then
        assertEquals(false, result)
        confirmVerified(formDraftDao)
    }

    @Test
    fun `doesDraftExist() throws SQLiteException`() = testCoroutineRule.runTest {
        // Given
        coEvery { formDraftDao.doesDraftExist(TEST_PROPERTY_ID) } throws SQLiteException()

        // When
        val result = repository.doesFormExist(TEST_PROPERTY_ID)

        // Then
        assertFalse(result)
        assertThrows(SQLiteException::class.java) { throw SQLiteException() }
        coVerify(exactly = 1) { formDraftDao.doesDraftExist(TEST_PROPERTY_ID) }
        confirmVerified(formDraftDao)
    }


    @Test
    fun `doesPropertyExist() nominal case`() = testCoroutineRule.runTest {
        // Given
        val testPropertyId = 1L
        coEvery { formDraftDao.doesPropertyExist(testPropertyId) } returns true

        // When
        val result = repository.doesPropertyExist(testPropertyId)

        // Then
        assertEquals(true, result)
        coVerify(exactly = 1) { formDraftDao.doesPropertyExist(testPropertyId) }
        confirmVerified(formDraftDao)
    }

    @Test
    fun `doesPropertyExist() with null id returns false`() = testCoroutineRule.runTest {
        // Given
        val testPropertyId: Long? = null

        // When
        val result = repository.doesPropertyExist(testPropertyId)

        // Then
        assertEquals(false, result)
        confirmVerified(formDraftDao)
    }

    @Test
    fun `doesPropertyExist() throws SQLiteException`() = testCoroutineRule.runTest {
        // Given
        coEvery { formDraftDao.doesPropertyExist(TEST_PROPERTY_ID) } throws SQLiteException()

        // When
        val result = repository.doesPropertyExist(TEST_PROPERTY_ID)

        // Then
        assertFalse(result)
        assertThrows(SQLiteException::class.java) { throw SQLiteException() }
        coVerify(exactly = 1) { formDraftDao.doesPropertyExist(TEST_PROPERTY_ID) }
        confirmVerified(formDraftDao)
    }


    @Test
    fun `getFormDraftEntityById() nominal case`() = testCoroutineRule.runTest {
        // Given
        val testPropertyId = 1L
        coEvery { formDraftDao.getPropertyFormById(testPropertyId) } returns TEST_FORM_DRAFT_WITH_DETAILS
        every {
            formDraftMapper.mapToFormDraftEntity(
                TEST_FORM_DRAFT_WITH_DETAILS.propertyForm,
                TEST_FORM_DRAFT_WITH_DETAILS.picturePreviews
            )
        } returns TEST_FORM_DRAFT_ENTITY

        // When
        val result = repository.getFormDraftEntityById(testPropertyId)

        // Then
        assertEquals(TEST_FORM_DRAFT_ENTITY, result)
        coVerify(exactly = 1) { formDraftDao.getPropertyFormById(testPropertyId) }
        verify(exactly = 1) { formDraftMapper.mapToFormDraftEntity(any(), any()) }
        confirmVerified(formDraftDao, formDraftMapper)
    }

    @Test
    fun `getDraftsCount() nominal case`() = testCoroutineRule.runTest {
        // Given
        coEvery { formDraftDao.getAddPropertyDraftsCount() } returns 4

        // When
        val result = repository.getFormsCount()

        // Then
        assertEquals(4, result)
        coVerify(exactly = 1) { formDraftDao.getAddPropertyDraftsCount() }
        confirmVerified(formDraftDao)
    }

    @Test
    fun `getDraftsWithFeaturePicture() returns list of FormWithTitleDateAndFeaturedPictureEntities`() =
        testCoroutineRule.runTest {
            // Given

            coEvery { formDraftDao.getFormsWithFeaturedPicture() } returns getTestFormWithTitleDateAndFeaturedPictures()
            every {
                formWithTitleDateAndFeaturedPictureMapper.mapToFormsWithTitleDateAndFeaturedPictureEntities(
                    getTestFormWithTitleDateAndFeaturedPictures()
                )
            } returns getTestFormWithTitleDateAndFeaturedPictureEntities()

            // When
            val result = repository.getFormsWithDetails()

            // Then
            assertEquals(getTestFormWithTitleDateAndFeaturedPictureEntities(), result)
            coVerify(exactly = 1) { formDraftDao.getFormsWithFeaturedPicture() }
            verify(exactly = 1) {
                formWithTitleDateAndFeaturedPictureMapper.mapToFormsWithTitleDateAndFeaturedPictureEntities(
                    getTestFormWithTitleDateAndFeaturedPictures()
                )
            }
            confirmVerified(formDraftDao, formWithTitleDateAndFeaturedPictureMapper)
        }

    @Test
    fun `update() nominal case`() = testCoroutineRule.runTest {
        // Given
        coJustRun {
            formDraftDao.update(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
            )
        }

        val testFormDraftEntity = getTestFormDraftEntity(TEST_PROPERTY_ID)

        // When
        repository.update(testFormDraftEntity)

        // Then
        coVerify(exactly = 1) {
            formDraftDao.update(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
            )
        }
        verify(exactly = 1) { formDraftMapper.mapToFormDraftDto(testFormDraftEntity) }
        confirmVerified(formDraftDao, formDraftMapper)
    }

    @Test
    fun `updateAddressValidity() nominal case`() = testCoroutineRule.runTest {
        // Given
        coJustRun { formDraftDao.updateAddressValidity(any(), any()) }

        // When
        repository.updateAddressValidity(TEST_PROPERTY_ID, true)

        // Then
        coVerify(exactly = 1) { formDraftDao.updateAddressValidity(TEST_PROPERTY_ID, true) }
        confirmVerified(formDraftDao)
    }

    @Test
    fun `delete() nominal case`() = testCoroutineRule.runTest {
        // When
        coEvery { formDraftDao.delete(TEST_PROPERTY_ID) } returns 1
        coEvery { picturePreviewDao.deleteAll(TEST_PROPERTY_ID) } returns 1
        val result = repository.delete(TEST_PROPERTY_ID)

        // Then
        assertTrue(result)
        coVerify(exactly = 1) {
            formDraftDao.delete(TEST_PROPERTY_ID)
            picturePreviewDao.deleteAll(TEST_PROPERTY_ID)
        }
        confirmVerified(formDraftDao)
    }

    @Test
    fun `delete() throws SQLiteException`() = testCoroutineRule.runTest {
        // Given
        coEvery { formDraftDao.delete(TEST_PROPERTY_ID) } returns null
        coEvery { picturePreviewDao.deleteAll(TEST_PROPERTY_ID) } returns null

        // When
        val result = repository.delete(TEST_PROPERTY_ID)

        // Then
        assertFalse(result)
        assertThrows(SQLiteException::class.java) { throw SQLiteException() }
        coVerify(exactly = 1) {
            formDraftDao.delete(TEST_PROPERTY_ID)
            picturePreviewDao.deleteAll(TEST_PROPERTY_ID)
        }
        confirmVerified(formDraftDao)
    }
}

// region test utils
fun getFormDraftWithDetails(id: Long) = FormDraftWithDetails(
    propertyForm = getTestFormDraftDto(id),
    picturePreviews = getTestPicturePreviewDtos(id)
)

fun getTestPicturePreviewDtos(id: Long) = getTestPicturePreviewEntities().map {
    PicturePreviewDto(
        id = it.id,
        propertyFormId = id,
        uri = it.uri,
        description = it.description,
        isFeatured = it.isFeatured,
    )
}

fun getTestFormWithTitleDateAndFeaturedPictureEntities() = buildList {
    add(
        FormWithDetailEntity(
            id = 1L,
            title = "Test Title 1",
            lastEditionDate = LocalDateTime.of(2021, 1, 1, 1, 1),
            featuredPicture = "saleDate",
            featuredPictureDescription = "Featured Picture Description 1"
        )
    )
    add(
        FormWithDetailEntity(
            id = 2L,
            title = "Test Title 2",
            lastEditionDate = LocalDateTime.of(2021, 2, 2, 2, 2),
            featuredPicture = "saleDate",
            featuredPictureDescription = "Featured Picture Description 2"
        )
    )
    add(
        FormWithDetailEntity(
            id = 3L,
            title = "Test Title 3",
            lastEditionDate = LocalDateTime.of(2021, 3, 3, 3, 3),
            featuredPicture = "saleDate",
            featuredPictureDescription = "Featured Picture Description 3"
        )
    )
}

fun getTestFormWithTitleDateAndFeaturedPictures() = buildList {
    add(
        FormWithTitleDateAndFeaturedPicture(
            formWithTitleAndLastEditionDateEntity = FormWithTitleAndLastEditionDateEntity(
                id = 1L,
                title = "Test Title 1",
                lastEditionDate = LocalDateTime.of(2021, 1, 1, 1, 1),
            ),
            featuredPicture = getTestPicturePreviewDtos(1L)[0],
        )
    )
    add(
        FormWithTitleDateAndFeaturedPicture(
            formWithTitleAndLastEditionDateEntity = FormWithTitleAndLastEditionDateEntity(
                id = 2L,
                title = "Test Title 2",
                lastEditionDate = LocalDateTime.of(2021, 2, 2, 2, 2),
            ),
            featuredPicture = getTestPicturePreviewDtos(1L)[1],
        )
    )
    add(
        FormWithTitleDateAndFeaturedPicture(
            formWithTitleAndLastEditionDateEntity = FormWithTitleAndLastEditionDateEntity(
                id = 3L,
                title = "Test Title 3",
                lastEditionDate = LocalDateTime.of(2021, 3, 3, 3, 3),
            ),
            featuredPicture = getTestPicturePreviewDtos(1L)[2],
        ),
    )
}

// endregion