package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.filter.model.PropertyMinMaxStatsEntity
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.property.PropertyRepository
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class GetMinMaxPriceAndSurfaceConvertedUseCaseTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val propertyRepository: PropertyRepository = mockk()
    private val convertToSquareFeetDependingOnLocaleUseCase: ConvertToSquareFeetDependingOnLocaleUseCase = mockk()
    private val convertPriceDependingOnLocaleUseCase: ConvertPriceDependingOnLocaleUseCase = mockk()

    private val getMinMaxPriceAndSurfaceConvertedUseCase = GetMinMaxPriceAndSurfaceConvertedUseCase(
        propertyRepository,
        convertToSquareFeetDependingOnLocaleUseCase,
        convertPriceDependingOnLocaleUseCase,
    )

    @Before
    fun setUp() {
        coEvery { propertyRepository.getMinMaxPricesAndSurfaces() } returns testPropertyMinMaxStatsEntity
        coEvery { convertPriceDependingOnLocaleUseCase.invoke(testPropertyMinMaxStatsEntity.maxPrice) } returns BigDecimal(
            1000000
        )
        coEvery { convertPriceDependingOnLocaleUseCase.invoke(testPropertyMinMaxStatsEntity.minPrice) } returns BigDecimal.ZERO
        every { convertToSquareFeetDependingOnLocaleUseCase.invoke(testPropertyMinMaxStatsEntity.maxSurface) } returns BigDecimal(
            1000
        )
        every { convertToSquareFeetDependingOnLocaleUseCase.invoke(testPropertyMinMaxStatsEntity.minSurface) } returns BigDecimal.ZERO
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        val result = getMinMaxPriceAndSurfaceConvertedUseCase.invoke()
        assertEquals(result, testPropertyMinMaxStatsEntity)
    }

    @Test
    fun `case with conversion for price and surface`() = testCoroutineRule.runTest {
        // Given
        coEvery { convertPriceDependingOnLocaleUseCase.invoke(testPropertyMinMaxStatsEntity.maxPrice) } returns BigDecimal(
            728804
        )
        coEvery { convertPriceDependingOnLocaleUseCase.invoke(testPropertyMinMaxStatsEntity.minPrice) } returns BigDecimal.ZERO
        every { convertToSquareFeetDependingOnLocaleUseCase.invoke(testPropertyMinMaxStatsEntity.maxSurface) } returns BigDecimal(
            93
        )
        every { convertToSquareFeetDependingOnLocaleUseCase.invoke(testPropertyMinMaxStatsEntity.minSurface) } returns BigDecimal.ZERO
        val result = getMinMaxPriceAndSurfaceConvertedUseCase.invoke()
        assertEquals(
            result, testPropertyMinMaxStatsEntity.copy(
                maxPrice = BigDecimal(728804),
                maxSurface = BigDecimal(93),
            )
        )
    }

    private val testPropertyMinMaxStatsEntity = PropertyMinMaxStatsEntity(
        minPrice = BigDecimal.ZERO,
        maxPrice = BigDecimal(1000000),
        minSurface = BigDecimal.ZERO,
        maxSurface = BigDecimal(1000),
    )
}