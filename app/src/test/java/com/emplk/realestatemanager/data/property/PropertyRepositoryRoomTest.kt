package com.emplk.realestatemanager.data.property

import android.database.sqlite.SQLiteException
import app.cash.turbine.test
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isFalse
import assertk.assertions.isNotNull
import assertk.assertions.isNull
import assertk.assertions.isTrue
import com.emplk.realestatemanager.data.property.location.LocationDao
import com.emplk.realestatemanager.data.property.location.LocationMapper
import com.emplk.realestatemanager.data.property.picture.PictureDao
import com.emplk.realestatemanager.data.property.picture.PictureMapper
import com.emplk.realestatemanager.ensuresDispatcher
import com.emplk.realestatemanager.fixtures.getPictureDtos
import com.emplk.realestatemanager.fixtures.getPropertyWithDetail
import com.emplk.realestatemanager.fixtures.getTestLocationDto
import com.emplk.realestatemanager.fixtures.getTestPropertyDto
import com.emplk.realestatemanager.fixtures.getTestPropertyEntity
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.coVerifyOrder
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PropertyRepositoryRoomTest {

    companion object {
        private const val TEST_PROPERTY_ID = 1L
        private const val TEST_SQ_LITE_EXCEPTION_MESSAGE = "Test SQLiteException"
        private val GET_ALL_PROPERTIES_WITH_DETAILS_FLOW: Flow<List<PropertyWithDetails>> = flowOf()
        private val GET_PROPERTY_WITH_DETAILS_FLOW: Flow<PropertyWithDetails> = flowOf()
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val propertyDao: PropertyDao = mockk()
    private val locationDao: LocationDao = mockk()
    private val pictureDao: PictureDao = mockk()
    private val propertyMapper: PropertyMapper = mockk()
    private val locationMapper: LocationMapper = mockk()
    private val pictureMapper: PictureMapper = mockk()

    private val propertyRepositoryRoom = PropertyRepositoryRoom(
        propertyDao,
        locationDao,
        pictureDao,
        propertyMapper,
        locationMapper,
        pictureMapper,
        testCoroutineRule.getTestCoroutineDispatcherProvider()
    )

    @Before
    fun setUp() {
        val testPropertyDto = getTestPropertyDto(TEST_PROPERTY_ID)
        val testLocationDto = getTestLocationDto(TEST_PROPERTY_ID)
        val testPictureDtos = getPictureDtos(TEST_PROPERTY_ID)

        every { propertyMapper.mapToDto(any()) } returns testPropertyDto
        coEvery { propertyDao.insert(testPropertyDto) }.ensuresDispatcher(
            testCoroutineRule.ioDispatcher,
            answersBlock = {
                TEST_PROPERTY_ID
            }
        )

        coEvery {
            propertyDao.update(testPropertyDto)
        } returns 1

        coEvery { locationDao.insert(testLocationDto) } returns 1L

        every { locationMapper.mapToDto(any(), TEST_PROPERTY_ID) } returns testLocationDto
        coEvery {
            locationDao.update(any(), any(), any(), any(), TEST_PROPERTY_ID)
        } returns 1

        every { pictureMapper.mapToDtoEntity(any(), TEST_PROPERTY_ID) } returnsMany testPictureDtos.map { it }
        coEvery {
            pictureDao.insert(testPictureDtos[0])
        } returns 1L

        coEvery {
            pictureDao.insert(testPictureDtos[1])
        } returns 2L

        coEvery {
            pictureDao.insert(testPictureDtos[2])
        } returns 3L

        coEvery { pictureDao.upsert(any()) } returnsMany testPictureDtos.map { it.id }

        every { pictureMapper.mapToDtoEntity(any(), TEST_PROPERTY_ID) } returnsMany testPictureDtos.map { it }
        coEvery { pictureDao.upsert(any()) } returnsMany testPictureDtos.map { it.id }

        every { propertyMapper.mapToDomainEntity(any(), any(), any()) } returns getTestPropertyEntity(TEST_PROPERTY_ID)
    }

    @Test
    fun `add - nominal case`() = testCoroutineRule.runTest {
        // Given
        val propertyEntity = getTestPropertyEntity(TEST_PROPERTY_ID)

        // When
        val result = propertyRepositoryRoom.add(propertyEntity)

        // Then
        assertThat(result).isEqualTo(TEST_PROPERTY_ID)
        coVerify(exactly = 1) { propertyDao.insert(any()) }
        confirmVerified(propertyDao)
    }

    @Test
    fun `add with error - throws SQLiteException`() = testCoroutineRule.runTest {
        // Given
        val propertyEntity = getTestPropertyEntity(TEST_PROPERTY_ID)
        coEvery { propertyDao.insert(any()) } throws SQLiteException(TEST_SQ_LITE_EXCEPTION_MESSAGE)

        // When
        val result = propertyRepositoryRoom.add(propertyEntity)

        // Then
        assertThat(result).isNull()
        coVerify(exactly = 1) { propertyDao.insert(any()) }
        assertThrows(SQLiteException::class.java) {
            throw SQLiteException(TEST_SQ_LITE_EXCEPTION_MESSAGE)
        }
        confirmVerified(propertyDao)
    }

    @Test
    fun `add property with details - nominal case`() = testCoroutineRule.runTest {
        // Given
        val propertyEntity = getTestPropertyEntity(TEST_PROPERTY_ID)

        // When
        val result = propertyRepositoryRoom.addPropertyWithDetails(propertyEntity)

        // Then
        assertThat(result).isNotNull()
        assertThat(result).isTrue()
        coVerify(exactly = 1) { propertyDao.insert(getTestPropertyDto(TEST_PROPERTY_ID)) }

        coVerifyOrder {
            locationDao.insert(getTestLocationDto(TEST_PROPERTY_ID))
            pictureDao.insert(getPictureDtos(TEST_PROPERTY_ID)[0])
            pictureDao.insert(getPictureDtos(TEST_PROPERTY_ID)[1])
            pictureDao.insert(getPictureDtos(TEST_PROPERTY_ID)[2])
        }
        confirmVerified(propertyDao)
    }

    @Test
    fun `add property with details - error with property insertion`() = testCoroutineRule.runTest {
        // Given
        coEvery { propertyDao.insert(any()) } throws SQLiteException(TEST_SQ_LITE_EXCEPTION_MESSAGE)

        // When
        val result = propertyRepositoryRoom.addPropertyWithDetails(getTestPropertyEntity(TEST_PROPERTY_ID))

        // Then
        assertThat(result).isFalse()
    }

    @Test
    fun `get properties as flow - nominal case`() = testCoroutineRule.runTest {
        // Given
        every { propertyDao.getPropertiesWithDetailsAsFlow() } returns GET_ALL_PROPERTIES_WITH_DETAILS_FLOW

        val propertyEntity = getTestPropertyEntity(TEST_PROPERTY_ID)
        every { propertyMapper.mapToDomainEntity(any(), any(), any()) } returns propertyEntity

        // When
        propertyRepositoryRoom.getPropertiesAsFlow().test { awaitComplete() }

        // Then
        coVerify(exactly = 1) { propertyDao.getPropertiesWithDetailsAsFlow() }
        confirmVerified(propertyDao)
    }

    @Test
    fun `get property by id as flow - nominal case`() = testCoroutineRule.runTest {
        // Given
        every { propertyDao.getPropertyByIdAsFlow(TEST_PROPERTY_ID) } returns GET_PROPERTY_WITH_DETAILS_FLOW

        val propertyEntity = getTestPropertyEntity(TEST_PROPERTY_ID)
        every { propertyMapper.mapToDomainEntity(any(), any(), any()) } returns propertyEntity

        // When
        propertyRepositoryRoom.getPropertyByIdAsFlow(TEST_PROPERTY_ID).test { awaitComplete() }

        // Then
        coVerify(exactly = 1) { propertyDao.getPropertyByIdAsFlow(TEST_PROPERTY_ID) }
        confirmVerified(propertyDao)
    }

    @Test
    fun `get property by id - nominal case`() = testCoroutineRule.runTest {
        //  Given
        coEvery { propertyDao.getPropertyById(TEST_PROPERTY_ID) } returns getPropertyWithDetail(TEST_PROPERTY_ID)

        // When
        val result = propertyRepositoryRoom.getPropertyById(TEST_PROPERTY_ID)

        // Then
        assertThat(result).isNotNull()
        assertThat(result).isEqualTo(getTestPropertyEntity(TEST_PROPERTY_ID))
        coVerify(exactly = 1) { propertyDao.getPropertyById(TEST_PROPERTY_ID) }
        confirmVerified(propertyDao)
    }

    @Test(expected = IllegalStateException::class)
    fun `get property by id - edge case with null return`() = testCoroutineRule.runTest {
        //  Given
        coEvery { propertyDao.getPropertyById(TEST_PROPERTY_ID) } returns null

        // When
        propertyRepositoryRoom.getPropertyById(TEST_PROPERTY_ID)

        // Then
        assertThrows(IllegalStateException::class.java) {
            throw IllegalStateException("Property with id $TEST_PROPERTY_ID not found")
        }
        coVerify(exactly = 1) { propertyDao.getPropertyById(TEST_PROPERTY_ID) }
        confirmVerified(propertyDao)
    }

    @Test
    fun `update - nominal case`() = testCoroutineRule.runTest {
        // Given
        val propertyEntity = getTestPropertyEntity(TEST_PROPERTY_ID)

        // When
        val result = propertyRepositoryRoom.update(propertyEntity)

        // Then
        assertThat(result).isNotNull()
        assertThat(result).isTrue()
        coVerify(exactly = 1) { propertyDao.update(getTestPropertyDto(TEST_PROPERTY_ID)) }
        coVerify(exactly = 1) { locationDao.update(any(), any(), any(), any(), TEST_PROPERTY_ID) }
        coVerify(exactly = 1) { pictureDao.upsert(getPictureDtos(TEST_PROPERTY_ID)[0]) }
        coVerify(exactly = 1) { pictureDao.upsert(getPictureDtos(TEST_PROPERTY_ID)[1]) }
        coVerify(exactly = 1) { pictureDao.upsert(getPictureDtos(TEST_PROPERTY_ID)[2]) }
        confirmVerified(propertyDao)
    }

    @Test
    fun `update with error - throws SQLiteException`() = testCoroutineRule.runTest {
        // Given
        coEvery { propertyDao.update(any()) } throws SQLiteException(TEST_SQ_LITE_EXCEPTION_MESSAGE)
        val propertyEntity = getTestPropertyEntity(TEST_PROPERTY_ID)

        // When
        val result = propertyRepositoryRoom.update(propertyEntity)

        // Then
        assertThat(result).isFalse()
        coVerify(exactly = 1) { propertyDao.update(any()) }
        assertThrows(SQLiteException::class.java) {
            throw SQLiteException(TEST_SQ_LITE_EXCEPTION_MESSAGE)
        }
        confirmVerified(propertyDao)
    }

}