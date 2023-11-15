package com.emplk.realestatemanager.ui.filter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isNotNull
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.filter.GetEntryDateByEntryDateStatusUseCase
import com.emplk.realestatemanager.domain.filter.GetFilteredPropertiesCountAsFlowUseCase
import com.emplk.realestatemanager.domain.filter.GetMinMaxPriceAndSurfaceUseCase
import com.emplk.realestatemanager.domain.filter.GetPropertyTypeForFilterUseCase
import com.emplk.realestatemanager.domain.filter.PropertyMinMaxStatsEntity
import com.emplk.realestatemanager.domain.filter.SetPropertiesFilterUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertToUsdDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.FormatPriceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property.amenity.type.GetAmenityTypeUseCase
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
    private val getEntryDateByEntryDateStatusUseCase: GetEntryDateByEntryDateStatusUseCase = mockk()
    private val getMinMaxPriceAndSurfaceUseCase: GetMinMaxPriceAndSurfaceUseCase = mockk()
    private val formatPriceToHumanReadableUseCase: FormatPriceToHumanReadableUseCase = mockk()
    private val convertSurfaceDependingOnLocaleUseCase: ConvertSurfaceDependingOnLocaleUseCase = mockk()
    private val convertToUsdDependingOnLocaleUseCase: ConvertToUsdDependingOnLocaleUseCase = mockk()
    private val convertSurfaceToSquareFeetDependingOnLocaleUseCase: ConvertSurfaceToSquareFeetDependingOnLocaleUseCase =
        mockk()
    private val getPropertyTypeForFilterUseCase: GetPropertyTypeForFilterUseCase = mockk()
    private val getAmenityTypeUseCase: GetAmenityTypeUseCase = mockk()
    private val setPropertiesFilterUseCase: SetPropertiesFilterUseCase = mockk()
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase = mockk()

    private lateinit var filterPropertiesViewModel: FilterPropertiesViewModel

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
        every { getEntryDateByEntryDateStatusUseCase.invoke(any()) } returns 1698758555
        coEvery { getMinMaxPriceAndSurfaceUseCase.invoke() } returns minMaxPriceAndSurface
        every { formatPriceToHumanReadableUseCase.invoke(minMaxPriceAndSurface.minPrice) } returns "$100000"
        every { formatPriceToHumanReadableUseCase.invoke(minMaxPriceAndSurface.maxPrice) } returns "$200000"
        every { convertSurfaceDependingOnLocaleUseCase.invoke(minMaxPriceAndSurface.minSurface) } returns BigDecimal(100)
        every { convertSurfaceDependingOnLocaleUseCase.invoke(minMaxPriceAndSurface.maxSurface) } returns BigDecimal(200)
        every { convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(any()) } returns BigDecimal(100)
        coEvery { convertToUsdDependingOnLocaleUseCase.invoke(any()) } returns BigDecimal(100000)
        every { getPropertyTypeForFilterUseCase.invoke() } returns propertyTypeMap
        every { getAmenityTypeUseCase.invoke() } returns amenityTypes
        justRun { setPropertiesFilterUseCase.invoke(any(), any(), any(), any(), any(), any(), any(), any()) }
        justRun { setNavigationTypeUseCase.invoke(any()) }

        filterPropertiesViewModel = FilterPropertiesViewModel(
            getFilteredPropertiesCountAsFlowUseCase,
            getEntryDateByEntryDateStatusUseCase,
            getMinMaxPriceAndSurfaceUseCase,
            formatPriceToHumanReadableUseCase,
            convertSurfaceDependingOnLocaleUseCase,
            convertToUsdDependingOnLocaleUseCase,
            convertSurfaceToSquareFeetDependingOnLocaleUseCase,
            getPropertyTypeForFilterUseCase,
            getAmenityTypeUseCase,
            setPropertiesFilterUseCase,
            setNavigationTypeUseCase
        )
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // Given
        filterPropertiesViewModel.onPropertyTypeSelected("House")
        filterPropertiesViewModel.onMinPriceChanged("100000")
        filterPropertiesViewModel.onMaxPriceChanged("200000")
        filterPropertiesViewModel.onMinSurfaceChanged("100")
        filterPropertiesViewModel.onMaxSurfaceChanged("200")
        filterPropertiesViewModel.onEntryDateStatusChanged(EntryDateState.ALL)
        filterPropertiesViewModel.onPropertySaleStateChanged(PropertySaleState.ALL)

        // When
        filterPropertiesViewModel.viewState.observeForTesting(this) { filterViewState ->

          //  (filterViewState.value!!.amenities[0] as AmenityViewState.AmenityCheckbox).onCheckBoxClicked(true)

            // Then
            assertThat(filterViewState.value!!).isEqualTo(testFilterViewState)
        }
    }

    private val propertyTypeMap = mapOf(
        1L to "House",
        2L to "Flat",
        3L to "Duplex",
        4L to "Penthouse",
        5L to "Villa",
        6L to "Manor",
        7L to "Other",
    )
    private val amenityTypes = AmenityType.values().toList()

    private val minMaxPriceAndSurface = PropertyMinMaxStatsEntity(
        minPrice = BigDecimal(100000),
        maxPrice = BigDecimal(200000),
        minSurface = BigDecimal(100),
        maxSurface = BigDecimal(200),
    )
    private val testFilterViewState: FilterViewState = FilterViewState(
        propertyType = "House",
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
                "100",
                "200",
            )
        ),
        minSurface = "100",
        maxSurface = "200",
        amenities = buildList {
            amenityTypes.forEach { amenityType ->
                add(
                    AmenityViewState.AmenityCheckbox(
                        id = amenityType.id,
                        name = amenityType.name,
                        isChecked = false,
                        onCheckBoxClicked = EquatableCallbackWithParam { },
                        iconDrawable = amenityType.iconDrawable,
                        stringRes = amenityType.stringRes,
                    )
                )
            }
        },
        propertyTypes = buildList {
            propertyTypeMap.forEach { (id, name) ->
                add(
                    PropertyTypeViewStateItem(
                        id,
                        name
                    )
                )
            }
        },
        entryDate = EntryDateState.ALL,
        availableForSale = PropertySaleState.ALL,
        filterButtonText = NativeText.Simple("3 properties"),
        isFilterButtonEnabled = true,
        onFilterClicked = EquatableCallback { },
        onCancelClicked = EquatableCallback { },
    )
}