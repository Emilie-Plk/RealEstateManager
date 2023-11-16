package com.emplk.realestatemanager.ui.list

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.current_property.SetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.filter.GetPropertiesFilterFlowUseCase
import com.emplk.realestatemanager.domain.filter.IsPropertyMatchingFiltersUseCase
import com.emplk.realestatemanager.domain.filter.PropertiesFilterEntity
import com.emplk.realestatemanager.domain.locale_formatting.currency.FormatPriceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.FormatAndRoundSurfaceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.GetPropertiesAsFlowUseCase
import com.emplk.realestatemanager.fixtures.getPropertyEntities
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import com.emplk.utils.TestCoroutineRule
import com.emplk.utils.observeForTesting
import io.mockk.coEvery
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class PropertiesViewModelTest {
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getPropertiesAsFlowUseCase: GetPropertiesAsFlowUseCase = mockk()
    private val setCurrentPropertyIdUseCase: SetCurrentPropertyIdUseCase = mockk()
    private val convertToSquareFeetDependingOnLocaleUseCase: ConvertToSquareFeetDependingOnLocaleUseCase = mockk()
    private val convertPriceDependingOnLocaleUseCase: ConvertPriceDependingOnLocaleUseCase = mockk()
    private val getRoundedHumanReadableSurfaceUseCase: FormatAndRoundSurfaceToHumanReadableUseCase = mockk()
    private val formatPriceToHumanReadableUseCase: FormatPriceToHumanReadableUseCase = mockk()
    private val getPropertiesFilterFlowUseCase: GetPropertiesFilterFlowUseCase = mockk()
    private val isPropertyMatchingFiltersUseCase: IsPropertyMatchingFiltersUseCase = mockk()
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase = mockk()

    private lateinit var propertiesViewModel: PropertiesViewModel

    @Before
    fun setUp() {
        justRun { setCurrentPropertyIdUseCase.invoke(any()) }
        every { convertToSquareFeetDependingOnLocaleUseCase.invoke(BigDecimal(500)) } returns BigDecimal(500)
        coEvery { convertPriceDependingOnLocaleUseCase.invoke(BigDecimal(1000000)) } returns BigDecimal(1000000)
        every { getRoundedHumanReadableSurfaceUseCase.invoke(BigDecimal(500)) } returns "500 sq ft"
        every { formatPriceToHumanReadableUseCase.invoke(BigDecimal(1000000)) } returns "$1,000,000"
        every { getPropertiesFilterFlowUseCase.invoke() } returns flowOf(null)
        every { isPropertyMatchingFiltersUseCase.invoke(any(), any(), any(), any(), any(), any(), any()) } returns true
        justRun { setNavigationTypeUseCase.invoke(any()) }

        propertiesViewModel = PropertiesViewModel(
            getPropertiesAsFlowUseCase,
            setCurrentPropertyIdUseCase,
            convertToSquareFeetDependingOnLocaleUseCase,
            convertPriceDependingOnLocaleUseCase,
            getRoundedHumanReadableSurfaceUseCase,
            formatPriceToHumanReadableUseCase,
            getPropertiesFilterFlowUseCase,
            isPropertyMatchingFiltersUseCase,
            setNavigationTypeUseCase
        )
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // When
        coEvery { getPropertiesAsFlowUseCase.invoke() } returns flowOf(emptyList())

        // Then
        propertiesViewModel.viewState.observeForTesting(this) {
            assertThat(it.value).isEqualTo(listOf(PropertiesViewState.EmptyState(onAddClick = EquatableCallback {})))
        }
    }

    @Test
    fun `3 properties give 3 view states`() = testCoroutineRule.runTest {
        coEvery { getPropertiesAsFlowUseCase.invoke() } returns flowOf(getPropertyEntities(3))
        propertiesViewModel.viewState.observeForTesting(this) {
            assertThat(it.value!!.size).isEqualTo(3)
            assertThat(it.value).isEqualTo(testViewStates)
        }
    }

    @Test
    fun `4 properties with price filtering give 1 view states`() = testCoroutineRule.runTest {
        val testPropertyEntity = getPropertyEntities(3).first().copy(
            price = BigDecimal(2000000),
        )
        coEvery { getPropertiesAsFlowUseCase.invoke() } returns flowOf(getPropertyEntities(3) + testPropertyEntity)
        every { getPropertiesFilterFlowUseCase.invoke() } returns flowOf(
            PropertiesFilterEntity(
                minMaxPrice = Pair(
                    BigDecimal(1500000),
                    BigDecimal(2000000)
                )
            )
        )
        coEvery { convertPriceDependingOnLocaleUseCase.invoke(BigDecimal(2000000)) } returns BigDecimal(2000000)
        every { getRoundedHumanReadableSurfaceUseCase.invoke(BigDecimal(500)) } returns "500 sq ft"
        every { formatPriceToHumanReadableUseCase.invoke(BigDecimal(2000000)) } returns "$2,000,000"
        every {
            isPropertyMatchingFiltersUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns false andThen false andThen false andThen true
        propertiesViewModel.viewState.observeForTesting(this) {
            assertThat(it.value!!.size).isEqualTo(1)
            assertThat((it.value!![0] as PropertiesViewState.Properties).humanReadablePrice).isEqualTo("$2,000,000")
        }
    }

    @Test
    fun `5 properties with type filtering give 2 view states`() = testCoroutineRule.runTest {
        val testPropertyEntity = getPropertyEntities(3).first().copy(
            type = "Villa",
        )
        val testPropertyEntity2 = getPropertyEntities(3).first().copy(
            type = "Villa",
        )
        coEvery { getPropertiesAsFlowUseCase.invoke() } returns flowOf(getPropertyEntities(3) + testPropertyEntity + testPropertyEntity2)
        every { getPropertiesFilterFlowUseCase.invoke() } returns flowOf(PropertiesFilterEntity(propertyType = "Villa"))

        every {
            isPropertyMatchingFiltersUseCase.invoke(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } returns false andThen false andThen false andThen true
        propertiesViewModel.viewState.observeForTesting(this) {
            assertThat(it.value!!.size).isEqualTo(2)
            assertThat((it.value!![0] as PropertiesViewState.Properties).propertyType).isEqualTo("Villa")
            assertThat((it.value!![1] as PropertiesViewState.Properties).propertyType).isEqualTo("Villa")
        }
    }

    @Test
    fun `on click event should set both current property id and navigation`() = testCoroutineRule.runTest {
        // Given
        coEvery { getPropertiesAsFlowUseCase.invoke() } returns flowOf(getPropertyEntities(3))
        propertiesViewModel.viewState.observeForTesting(this) {
            // When
            (it.value!![0] as PropertiesViewState.Properties).onClickEvent.invoke()
            // Then
            verify(exactly = 1) {
                setCurrentPropertyIdUseCase.invoke(1L)
                setNavigationTypeUseCase.invoke(NavigationFragmentType.DETAIL_FRAGMENT)
            }
        }
    }


    private val testViewStates = buildList {
        getPropertyEntities(3).forEach {
            add(PropertiesViewState.Properties(
                id = it.id,
                propertyType = it.type,
                featuredPicture = it.pictures.firstOrNull()?.let { featuredPic -> NativePhoto.Uri(featuredPic.uri) }
                    ?: NativePhoto.Resource(R.drawable.baseline_villa_24),
                address = it.location.address,
                humanReadablePrice = "$1,000,000",
                isSold = false,
                room = NativeText.Argument(R.string.rooms_nb_short_version, it.rooms),
                bathroom = NativeText.Argument(R.string.bathrooms_nb_short_version, it.bathrooms),
                bedroom = NativeText.Argument(R.string.bedrooms_nb_short_version, it.bedrooms),
                humanReadableSurface = "500 sq ft",
                entryDate = it.entryDate,
                amenities = it.amenities,
                onClickEvent = EquatableCallback {},
            )
            )
        }
    }

}