package com.emplk.realestatemanager.data.property

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import assertk.assertions.isTrue
import com.emplk.realestatemanager.data.property.location.LocationDao
import com.emplk.realestatemanager.data.property.location.LocationMapper
import com.emplk.realestatemanager.data.property.picture.PictureDao
import com.emplk.realestatemanager.data.property.picture.PictureMapper
import com.emplk.realestatemanager.fixtures.getPictureDtos
import com.emplk.realestatemanager.fixtures.getTestLocationDto
import com.emplk.realestatemanager.fixtures.getTestPropertyDto
import com.emplk.realestatemanager.fixtures.getTestPropertyEntity
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class PropertyRepositoryRoomTest {

    companion object {
        private const val TEST_PROPERTY_ID = 1L
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
        every { propertyMapper.mapToDto(any()) } returns testPropertyDto
        coEvery {
            propertyDao.insert(testPropertyDto)
        } returns TEST_PROPERTY_ID

        coEvery {
            propertyDao.update(testPropertyDto)
        } returns 1

        val testLocationDto = getTestLocationDto(TEST_PROPERTY_ID)
        every { locationMapper.mapToDto(any(), TEST_PROPERTY_ID) } returns testLocationDto
        coEvery {
            locationDao.update(testLocationDto)
        } returns 1

        val testPictureDtos = getPictureDtos(TEST_PROPERTY_ID)
        every { pictureMapper.mapToDtoEntity(any(), TEST_PROPERTY_ID) } returnsMany testPictureDtos.map { it }
        coEvery { pictureDao.upsert(any()) } returnsMany testPictureDtos.map { it.id }
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
    fun `update - nominal case`() = testCoroutineRule.runTest {
        // Given
        val propertyEntity = getTestPropertyEntity(TEST_PROPERTY_ID)

        // When
        val result = propertyRepositoryRoom.update(propertyEntity)

        // Then
        assertThat(result).isNotNull()
        assertThat(result).isTrue()
        coVerify(exactly = 1) { propertyDao.update(getTestPropertyDto(TEST_PROPERTY_ID)) }
        coVerify(exactly = 1) { locationDao.update(getTestLocationDto(TEST_PROPERTY_ID)) }
        coVerify(exactly = 1) { pictureDao.upsert(getPictureDtos(TEST_PROPERTY_ID)[0]) }
        coVerify(exactly = 1) { pictureDao.upsert(getPictureDtos(TEST_PROPERTY_ID)[1]) }
        coVerify(exactly = 1) { pictureDao.upsert(getPictureDtos(TEST_PROPERTY_ID)[2]) }
        confirmVerified(propertyDao)
    }

}