package com.emplk.realestatemanager.ui.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.filter.ConvertSearchedEntryDateRangeToEpochMilliUseCase
import com.emplk.realestatemanager.domain.filter.GetFilteredPropertiesCountAsFlowUseCase
import com.emplk.realestatemanager.domain.filter.GetMinMaxPriceAndSurfaceConvertedUseCase
import com.emplk.realestatemanager.domain.filter.OptimizeValuesForFilteringUseCase
import com.emplk.realestatemanager.domain.filter.SearchedEntryDateRange
import com.emplk.realestatemanager.domain.filter.SetPropertiesFilterUseCase
import com.emplk.realestatemanager.domain.filter.model.PropertyMinMaxStatsEntity
import com.emplk.realestatemanager.domain.locale_formatting.currency.FormatPriceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertSurfaceToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToUsdDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.FormatAndRoundSurfaceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property.amenity.type.GetAmenityTypeUseCase
import com.emplk.realestatemanager.domain.property_type.GetPropertyTypeUseCase
import com.emplk.realestatemanager.fixtures.getTestPropertyTypesForFilter
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.add.type.PropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.NativeText
import com.emplk.utils.TestCoroutineRule
import com.emplk.utils.observeForTesting
import io.mockk.coEvery
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class FilterPropertiesViewModelTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getFilteredPropertiesCountAsFlowUseCase: GetFilteredPropertiesCountAsFlowUseCase = mockk()
    private val convertSearchedEntryDateRangeToEpochMilliUseCase: ConvertSearchedEntryDateRangeToEpochMilliUseCase =
        mockk()
    private val getMinMaxPriceAndSurfaceConvertedUseCase: GetMinMaxPriceAndSurfaceConvertedUseCase = mockk()
    private val formatPriceToHumanReadableUseCase: FormatPriceToHumanReadableUseCase = mockk()
    private val formatAndRoundSurfaceToHumanReadableUseCase: FormatAndRoundSurfaceToHumanReadableUseCase = mockk()
    private val convertToUsdDependingOnLocaleUseCase: ConvertToUsdDependingOnLocaleUseCase = mockk()
    private val convertSurfaceToSquareFeetDependingOnLocaleUseCase: ConvertSurfaceToSquareFeetDependingOnLocaleUseCase =
        mockk()
    private val getPropertyTypeUseCase: GetPropertyTypeUseCase = mockk()
    private val getAmenityTypeUseCase: GetAmenityTypeUseCase = mockk()
    private val setPropertiesFilterUseCase: SetPropertiesFilterUseCase = mockk()
    private val optimizeValuesForFilteringUseCase: OptimizeValuesForFilteringUseCase = mockk()
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase = mockk()
    private lateinit var viewModel: FilterPropertiesViewModel

    @Before
    fun setUp() {
        coEvery {
            getFilteredPropertiesCountAsFlowUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns flowOf(3)
        // Price
        every { optimizeValuesForFilteringUseCase.invoke(BigDecimal(100000), BigDecimal(200000)) } returns Pair(
            BigDecimal(100000),
            BigDecimal(200000)
        )
        // Surface
        every { optimizeValuesForFilteringUseCase.invoke(BigDecimal(100), BigDecimal(200)) } returns Pair(
            BigDecimal(100),
            BigDecimal(200)
        )
        every { optimizeValuesForFilteringUseCase.invoke(BigDecimal.ZERO, BigDecimal.ZERO) } returns Pair(
            BigDecimal.ZERO,
            BigDecimal.ZERO
        )
        every { convertSearchedEntryDateRangeToEpochMilliUseCase.invoke(any()) } returns 1698758555
        coEvery { getMinMaxPriceAndSurfaceConvertedUseCase.invoke() } returns minMaxPriceAndSurface
        every { formatPriceToHumanReadableUseCase.invoke(minMaxPriceAndSurface.minPrice) } returns "$100000"
        every { formatPriceToHumanReadableUseCase.invoke(minMaxPriceAndSurface.maxPrice) } returns "$200000"
        every { convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(BigDecimal(100)) } returns BigDecimal(100)
        every { convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(BigDecimal(200)) } returns BigDecimal(200)
        every { convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(BigDecimal.ZERO) } returns BigDecimal.ZERO
        every { formatAndRoundSurfaceToHumanReadableUseCase.invoke(minMaxPriceAndSurface.minSurface) } returns "100 sq ft"
        every { formatAndRoundSurfaceToHumanReadableUseCase.invoke(minMaxPriceAndSurface.maxSurface) } returns "200 sq ft"
        coEvery { convertToUsdDependingOnLocaleUseCase.invoke(BigDecimal(100000)) } returns BigDecimal(100000)
        coEvery { convertToUsdDependingOnLocaleUseCase.invoke(BigDecimal(200000)) } returns BigDecimal(200000)
        coEvery { convertToUsdDependingOnLocaleUseCase.invoke(BigDecimal.ZERO) } returns BigDecimal.ZERO
        every { getPropertyTypeUseCase.invoke() } returns getTestPropertyTypesForFilter()
        every { getAmenityTypeUseCase.invoke() } returns amenityTypes
        justRun { setPropertiesFilterUseCase.invoke(any(), any(), any(), any(), any(), any(), any(), any()) }
        justRun { setNavigationTypeUseCase.invoke(any()) }

        viewModel = FilterPropertiesViewModel(
            getFilteredPropertiesCountAsFlowUseCase,
            convertSearchedEntryDateRangeToEpochMilliUseCase,
            getMinMaxPriceAndSurfaceConvertedUseCase,
            formatPriceToHumanReadableUseCase,
            formatAndRoundSurfaceToHumanReadableUseCase,
            convertToUsdDependingOnLocaleUseCase,
            convertSurfaceToSquareFeetDependingOnLocaleUseCase,
            getPropertyTypeUseCase,
            getAmenityTypeUseCase,
            setPropertiesFilterUseCase,
            optimizeValuesForFilteringUseCase,
            setNavigationTypeUseCase
        )
    }

    @Test
    fun `nominal case`() = testCoroutineRule.runTest {
        // Given
        coEvery {
            getFilteredPropertiesCountAsFlowUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns flowOf(3)
        viewModel.onPropertyTypeSelected("House")
        viewModel.onMinPriceChanged("100000")
        viewModel.onMaxPriceChanged("200000")
        viewModel.onMinSurfaceChanged("100")
        viewModel.onMaxSurfaceChanged("200")
        viewModel.onEntryDateRangeStatusChanged(SearchedEntryDateRange.ALL)
        viewModel.onPropertySaleStateChanged(PropertySaleState.ALL)

        // When
        viewModel.viewState.observeForTesting(this) {

            // Then
            assertEquals(testFilterViewState, it.value)
        }
    }

    @Test
    fun `on reset filter - should reset viewState`() = testCoroutineRule.runTest {
        // Given
        viewModel.onPropertyTypeSelected("House")
        viewModel.onMinPriceChanged("100000")
        viewModel.onMaxPriceChanged("200000")
        viewModel.onMinSurfaceChanged("100")
        viewModel.onMaxSurfaceChanged("200")
        viewModel.onEntryDateRangeStatusChanged(SearchedEntryDateRange.ALL)
        viewModel.onPropertySaleStateChanged(PropertySaleState.ALL)
        coEvery {
            getFilteredPropertiesCountAsFlowUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns flowOf(3)

        // When
        viewModel.onResetFilters()
        viewModel.viewState.observeForTesting(this) {
            // Then
            assertEquals(testEmptyViewState, it.value)
        }
    }

    private val amenityTypes = AmenityType.values().toList()

    private val minMaxPriceAndSurface = PropertyMinMaxStatsEntity(
        minPrice = BigDecimal(100000),
        maxPrice = BigDecimal(200000),
        minSurface = BigDecimal(100),
        maxSurface = BigDecimal(200),
    )
    private val testFilterViewState: FilterViewState = FilterViewState(
        propertyType = R.string.type_house,
        priceRange = NativeText.Arguments(
            R.string.surface_or_price_range,
            listOf(
                "$100000",
                "$200000",
            )
        ),
        minPrice = "100000",
        maxPrice = "200000",
        surfaceRange = NativeText.Arguments(
            R.string.surface_or_price_range,
            listOf(
                "100 sq ft",
                "200 sq ft",
            )
        ),
        minSurface = "100",
        maxSurface = "200",
        amenities =
        amenityTypes.map { amenityType ->
            AmenityViewState.AmenityCheckbox(
                id = amenityType.id,
                name = amenityType.name,
                isChecked = false,
                onCheckBoxClicked = EquatableCallbackWithParam { },
                iconDrawable = amenityType.iconDrawable,
                stringRes = amenityType.stringRes,
            )
        },
        propertyTypes = getTestPropertyTypesForFilter().map {
            PropertyTypeViewStateItem(
                id = it.id,
                name = NativeText.Resource(it.stringRes),
                databaseName = it.databaseName
            )
        },
        entryDate = SearchedEntryDateRange.ALL,
        availableForSale = PropertySaleState.ALL,
        filterButtonText = NativeText.Arguments(
            R.string.filter_button_nb_properties,
            listOf("3")
        ),
        isFilterButtonEnabled = true,
        onFilterClicked = EquatableCallback { },
        onCancelClicked = EquatableCallback { },
    )

    private val testEmptyViewState: FilterViewState = FilterViewState(
        propertyType = null,
        priceRange = NativeText.Arguments(
            R.string.surface_or_price_range,
            listOf(
                "$100000",
                "$200000",
            )
        ),
        minPrice = "",
        maxPrice = "",
        surfaceRange = NativeText.Arguments(
            R.string.surface_or_price_range,
            listOf(
                "100 sq ft",
                "200 sq ft",
            )
        ),
        minSurface = "",
        maxSurface = "",
        amenities = amenityTypes.map { amenityType ->
            AmenityViewState.AmenityCheckbox(
                id = amenityType.id,
                name = amenityType.name,
                isChecked = false,
                onCheckBoxClicked = EquatableCallbackWithParam { },
                iconDrawable = amenityType.iconDrawable,
                stringRes = amenityType.stringRes,
            )
        },
        propertyTypes = getTestPropertyTypesForFilter().map {
            PropertyTypeViewStateItem(
                id = it.id,
                name = NativeText.Resource(it.stringRes),
                databaseName = it.databaseName
            )
        },
        entryDate = null,
        availableForSale = null,
        filterButtonText = NativeText.Arguments(
            R.string.filter_button_nb_properties,
            listOf("3")
        ),
        isFilterButtonEnabled = true,
        onFilterClicked = EquatableCallback { },
        onCancelClicked = EquatableCallback { },
    )
}