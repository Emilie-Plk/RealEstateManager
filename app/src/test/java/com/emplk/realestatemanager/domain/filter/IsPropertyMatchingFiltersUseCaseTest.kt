package com.emplk.realestatemanager.domain.filter

import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.fixtures.testFixedClock
import com.emplk.realestatemanager.ui.filter.PropertySaleState
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.Parameterized
import java.math.BigDecimal
import java.time.Clock
import java.time.LocalDateTime

@RunWith(Parameterized::class)
class IsPropertyMatchingFiltersUseCaseTest(
    private val type: String?,
    private val price: BigDecimal,
    private val surface: BigDecimal,
    private val amenities: List<AmenityType>,
    private val entryDate: LocalDateTime,
    private val isSold: Boolean,
    private val propertiesFilter: PropertiesFilterEntity?,
    private val expectedMatch: Boolean,
) {

    private val clock: Clock = testFixedClock
    private val isPropertyMatchingFiltersUseCase = IsPropertyMatchingFiltersUseCase(clock)

    companion object {
        @JvmStatic
        @Parameterized.Parameters(
            name = "{index}: " +
                    "Given property type [{0}]," +
                    "price [{1}]," +
                    "surface [{2}] sq ft, " +
                    "amenities [{3}], " +
                    "entryDate [{4}]," +
                    "is sold: [{5}], " +
                    "and propertiesFilter=[{6}]," +
                    "Then it should match: [{7}]"
        )
        fun getValue() = listOf(
            arrayOf(
                null,
                BigDecimal(0),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.now(testFixedClock),
                false,
                null,
                true
            ),
            arrayOf(
                "Villa",
                BigDecimal(0),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.now(testFixedClock),
                false,
                null,
                true
            ),
            arrayOf(
                "Villa",
                BigDecimal(0),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.now(testFixedClock),
                false,
                PropertiesFilterEntity(propertyType = "Villa"),
                true
            ),
            arrayOf(
                "Villa",
                BigDecimal(0),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.now(testFixedClock),
                false,
                PropertiesFilterEntity(propertyType = "House"),
                false
            ),
            arrayOf(
                "Villa",
                BigDecimal(0),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.now(testFixedClock),
                false,
                PropertiesFilterEntity(propertyType = null),
                true
            ),
            arrayOf(
                "Villa",
                BigDecimal(0),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.now(testFixedClock),
                false,
                PropertiesFilterEntity(propertyType = null),
                true
            ),
            arrayOf(
                "Villa",
                BigDecimal(0),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.now(testFixedClock),
                false,
                PropertiesFilterEntity(propertyType = "Villa", minMaxPrice = Pair(BigDecimal(0), BigDecimal(0))),
                true
            ),
            arrayOf(
                "Villa",
                BigDecimal(0),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.now(testFixedClock),
                false,
                PropertiesFilterEntity(propertyType = "Villa", minMaxPrice = Pair(BigDecimal(0), BigDecimal(1000000))),
                true
            ),
            arrayOf(
                "Villa",
                BigDecimal(0),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.now(testFixedClock),
                false,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    minMaxPrice = Pair(BigDecimal(1000000), BigDecimal(2000000))
                ),
                false
            ),
            arrayOf(
                "Villa",
                BigDecimal(0),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.now(testFixedClock),
                false,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    minMaxPrice = Pair(BigDecimal(1000000), BigDecimal(2000000))
                ),
                false
            ),
            arrayOf(
                "Villa",
                BigDecimal(0),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.now(testFixedClock),
                false,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    minMaxPrice = Pair(BigDecimal(1000000), BigDecimal(2000000))
                ),
                false
            ),
            arrayOf(
                "Villa",
                BigDecimal(1000000),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.now(testFixedClock),
                false,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    minMaxPrice = Pair(BigDecimal(1000000), BigDecimal(2000000))
                ),
                true
            ),
            arrayOf(
                "Villa",
                BigDecimal(2000000),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.now(testFixedClock),
                false,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    minMaxPrice = Pair(BigDecimal(1000000), BigDecimal(2000000))
                ),
                true
            ),
            arrayOf(
                "Villa",
                BigDecimal(3000000),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.now(testFixedClock),
                false,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    minMaxPrice = Pair(BigDecimal(1000000), BigDecimal(2000000))
                ),
                false
            ),
            arrayOf(
                "Villa",
                BigDecimal(0),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.of(2023, 10, 16, 16, 20),
                false,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    minMaxPrice = Pair(BigDecimal(1000000), BigDecimal(2000000)),
                    entryDate = EntryDateState.ALL
                ),
                false
            ),
            arrayOf(
                "Villa",
                BigDecimal(0),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.of(2023, 11, 16, 16, 20),
                false,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    minMaxPrice = Pair(BigDecimal(1000000), BigDecimal(2000000)),
                    entryDate = EntryDateState.LESS_THAN_1_YEAR
                ),
                false
            ),
            arrayOf(
                "Villa",
                BigDecimal(0),
                BigDecimal(0),
                emptyList<AmenityType>(),
                LocalDateTime.of(2022, 10, 16, 16, 20),
                false,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    minMaxPrice = Pair(BigDecimal(1000000), BigDecimal(2000000)),
                    entryDate = EntryDateState.LESS_THAN_1_YEAR
                ),
                false
            ),
            arrayOf(
                "Villa",
                BigDecimal(1200000),
                BigDecimal(500),
                emptyList<AmenityType>(),
                LocalDateTime.of(2021, 10, 16, 16, 20),
                false,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    minMaxPrice = Pair(BigDecimal(1000000), BigDecimal(2000000)),
                    entryDate = EntryDateState.LESS_THAN_1_YEAR
                ),
                false
            ),
            arrayOf(
                "Villa",
                BigDecimal(1200000),
                BigDecimal(500),
                listOf<AmenityType>(AmenityType.SCHOOL),
                LocalDateTime.of(2021, 10, 16, 16, 20),
                false,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    amenities = listOf(AmenityType.SCHOOL),
                    minMaxPrice = Pair(BigDecimal(1000000), BigDecimal(2000000)),
                    entryDate = EntryDateState.LESS_THAN_1_YEAR
                ),
                false
            ),
            arrayOf(
                "Villa",
                BigDecimal(1200000),
                BigDecimal(500),
                listOf<AmenityType>(AmenityType.SCHOOL),
                LocalDateTime.of(2021, 10, 16, 16, 20),
                false,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    amenities = listOf(AmenityType.GYM),
                    minMaxPrice = Pair(BigDecimal(1000000), BigDecimal(2000000)),
                    entryDate = EntryDateState.LESS_THAN_1_YEAR
                ),
                false
            ),
            arrayOf(
                "Villa",
                BigDecimal(1200000),
                BigDecimal(500),
                listOf<AmenityType>(AmenityType.SCHOOL, AmenityType.GYM),
                LocalDateTime.of(2021, 10, 16, 16, 20),
                false,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    amenities = listOf(AmenityType.PARK),
                    minMaxPrice = Pair(BigDecimal(1000000), BigDecimal(2000000)),
                    entryDate = EntryDateState.LESS_THAN_1_YEAR
                ),
                false
            ),
            arrayOf(
                "Villa",
                BigDecimal(1200000),
                BigDecimal(500),
                listOf<AmenityType>(AmenityType.SCHOOL, AmenityType.GYM),
                LocalDateTime.of(2021, 10, 16, 16, 20),
                false,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    amenities = listOf(AmenityType.SCHOOL),
                    minMaxPrice = Pair(BigDecimal(1200000), BigDecimal(2000000)),
                    entryDate = EntryDateState.LESS_THAN_1_YEAR
                ),
                false
            ),
            // matching is sold
            arrayOf(
                "Villa",
                BigDecimal(1200000),
                BigDecimal(500),
                listOf<AmenityType>(AmenityType.SCHOOL, AmenityType.GYM),
                LocalDateTime.now(testFixedClock),
                true,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    amenities = listOf(AmenityType.SCHOOL),

                    availableForSale = PropertySaleState.ALL
                ),
                true
            ),
            arrayOf(
                "Villa",
                BigDecimal(0),
                BigDecimal(0),
                listOf(AmenityType.SCHOOL, AmenityType.GYM),
                LocalDateTime.now(testFixedClock),
                true,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    amenities = listOf(AmenityType.SCHOOL),
                    entryDate = EntryDateState.LESS_THAN_1_YEAR,
                    availableForSale = PropertySaleState.FOR_SALE
                ),
                false
            ),
            arrayOf(
                "Villa",
                BigDecimal(0),
                BigDecimal(0),
                listOf<AmenityType>(AmenityType.SCHOOL, AmenityType.GYM),
                LocalDateTime.now(testFixedClock),
                true,
                PropertiesFilterEntity(
                    propertyType = "Villa",
                    amenities = listOf(AmenityType.SCHOOL),
                    minMaxPrice = Pair(BigDecimal(1000000), BigDecimal(2000000)),
                    availableForSale = PropertySaleState.SOLD
                ),
                false
            ),
        )
    }

    @Test
    fun test() {
        assertEquals(
            expectedMatch,
            isPropertyMatchingFiltersUseCase.invoke(
                type,
                price,
                surface,
                amenities,
                entryDate,
                isSold,
                propertiesFilter
            )
        )
    }
}