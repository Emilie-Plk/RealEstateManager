package com.emplk.realestatemanager.domain.filter

import app.cash.turbine.test
import com.emplk.realestatemanager.domain.filter.model.SearchEntity
import com.emplk.realestatemanager.domain.property.PropertyRepository
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.fixtures.testFixedClock
import com.emplk.realestatemanager.ui.filter.PropertySaleState
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.ZoneId

class GetFilteredPropertiesCountAsFlowUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val propertyRepository: PropertyRepository = mockk()

    private val getFilteredPropertiesCountAsFlowUseCase = GetFilteredPropertiesCountAsFlowUseCase(
        propertyRepository,
        testFixedClock,
    )

    @Before
    fun setUp() {
        coEvery {
            propertyRepository.getFilteredPropertiesCountRawQuery(
                SearchEntity(
                    propertyType = "House",
                    minPrice = BigDecimal.ZERO,
                    maxPrice = BigDecimal(1000000),
                    minSurface = BigDecimal.ZERO,
                    maxSurface = BigDecimal(1000),
                    amenitySchool = true,
                    amenityPark = false,
                    amenityShopping = false,
                    amenityRestaurant = false,
                    amenityConcierge = false,
                    amenityGym = false,
                    amenityTransport = false,
                    amenityHospital = false,
                    amenityLibrary = false,
                    entryDateEpochMin = null,
                    entryDateEpochMax = LocalDateTime.now(testFixedClock).atZone(ZoneId.systemDefault()).toInstant()
                        .toEpochMilli(),
                    isSold = null,
                )
            )
        } returns flowOf(5)
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // When
        getFilteredPropertiesCountAsFlowUseCase.invoke(
            propertyType = "House",
            minPrice = BigDecimal.ZERO,
            maxPrice = BigDecimal(1000000),
            minSurface = BigDecimal.ZERO,
            maxSurface = BigDecimal(1000),
            amenities = listOf(AmenityType.SCHOOL),
            entryDateMin = null,
            propertySaleState = PropertySaleState.ALL,
        ).test {
            // Then
            assertEquals(5, awaitItem())
            awaitComplete()
        }
    }
}