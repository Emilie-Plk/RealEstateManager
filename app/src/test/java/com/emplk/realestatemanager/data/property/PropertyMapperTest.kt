package com.emplk.realestatemanager.data.property

import com.emplk.realestatemanager.data.property.location.LocationMapper
import com.emplk.realestatemanager.data.property.picture.PictureMapper
import com.emplk.realestatemanager.fixtures.getTestLocationDto
import com.emplk.realestatemanager.fixtures.getTestLocationEntity
import com.emplk.realestatemanager.fixtures.getTestPictureEntities
import com.emplk.realestatemanager.fixtures.getTestPropertyDto
import com.emplk.realestatemanager.fixtures.getTestPropertyEntity
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class PropertyMapperTest {

    private val locationMapper: LocationMapper = mockk()
    private val pictureMapper: PictureMapper = mockk()

    private val propertyMapper = PropertyMapper(locationMapper, pictureMapper)

    @Before
    fun setUp() {
        every { locationMapper.mapToDomainEntity(any()) } returns getTestLocationEntity()
        every { pictureMapper.mapToDomainEntities(any()) } returns getTestPictureEntities()
    }

    @Test
    fun `mapToDto nominal case`() {
        val result = propertyMapper.mapToDto(getTestPropertyEntity(1L))

        assertEquals(getTestPropertyDto(1L), result)
        confirmVerified(locationMapper, pictureMapper)
    }

    @Test
    fun `mapToDomainEntity nominal case`() {
        val result = propertyMapper.mapToDomainEntity(
            getTestPropertyDto(1L),
            getTestLocationDto(1L),
            listOf(mockk())
        )

        assertEquals(getTestPropertyEntity(1L), result)
        verify(exactly = 1) {
            locationMapper.mapToDomainEntity(any())
            pictureMapper.mapToDomainEntities(any())
        }
        confirmVerified(locationMapper, pictureMapper)
    }
}