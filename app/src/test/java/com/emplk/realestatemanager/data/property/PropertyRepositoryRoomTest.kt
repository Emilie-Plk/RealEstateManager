package com.emplk.realestatemanager.data.property

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.emplk.realestatemanager.data.property.location.LocationDao
import com.emplk.realestatemanager.data.property.location.LocationMapper
import com.emplk.realestatemanager.data.property.picture.PictureDao
import com.emplk.realestatemanager.data.property.picture.PictureMapper
import com.emplk.realestatemanager.ensuresDispatcher
import com.emplk.realestatemanager.fixtures.getTestPropertyDto
import com.emplk.realestatemanager.fixtures.getTestPropertyEntity
import com.emplk.realestatemanager.fixtures.mapPropertyEntityToDto
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
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
        coEvery {
            propertyDao.insert(mapPropertyEntityToDto(getTestPropertyEntity(TEST_PROPERTY_ID)))
        }.ensuresDispatcher(testCoroutineRule.ioDispatcher) { TEST_PROPERTY_ID }
    }

    @Test
    fun `add - nominal case`() = testCoroutineRule.runTest {
        // Given
        val propertyEntity = getTestPropertyEntity(TEST_PROPERTY_ID)
        every { propertyMapper.mapToDto(propertyEntity) } returns getTestPropertyDto(TEST_PROPERTY_ID)

        // When
        val result = propertyRepositoryRoom.add(propertyEntity)

        // Then
        assertThat(result).isEqualTo(TEST_PROPERTY_ID)
    }

}