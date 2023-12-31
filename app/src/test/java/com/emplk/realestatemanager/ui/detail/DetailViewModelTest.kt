package com.emplk.realestatemanager.ui.detail

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import assertk.assertions.isEqualTo
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.currency_rate.GetLastUpdatedCurrencyRateDateUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.currency.FormatPriceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.FormatAndRoundSurfaceToHumanReadableUseCase
import com.emplk.realestatemanager.domain.map_picture.GenerateMapUrlWithApiKeyUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.GetCurrentPropertyUseCase
import com.emplk.realestatemanager.domain.property_type.GetStringResourceForTypeUseCase
import com.emplk.realestatemanager.fixtures.getTestPropertyEntity
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.detail.picture_banner.PictureBannerViewState
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
import java.util.Locale


class DetailViewModelTest {

    companion object {
        private val US = Locale.US
        private const val TEST_PROPERTY_ID = 1L
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getCurrentPropertyUseCase: GetCurrentPropertyUseCase = mockk()
    private val formatPriceToHumanReadableUseCase: FormatPriceToHumanReadableUseCase = mockk()
    private val convertPriceDependingOnLocaleUseCase: ConvertPriceDependingOnLocaleUseCase = mockk()
    private val getLastUpdatedCurrencyRateDateUseCase: GetLastUpdatedCurrencyRateDateUseCase = mockk()
    private val getLocaleUseCase: GetLocaleUseCase = mockk()
    private val formatAndRoundSurfaceToHumanReadableUseCase: FormatAndRoundSurfaceToHumanReadableUseCase = mockk()
    private val convertToSquareFeetDependingOnLocaleUseCase: ConvertToSquareFeetDependingOnLocaleUseCase = mockk()
    private val generateMapUrlWithApiKeyUseCase: GenerateMapUrlWithApiKeyUseCase = mockk()
    private val getStringResourceForTypeUseCase: GetStringResourceForTypeUseCase = mockk()
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase = mockk()

    private lateinit var viewModel: DetailViewModel

    @Before
    fun setUp() {
        Locale.setDefault(US)
        every { getCurrentPropertyUseCase.invoke() } returns flowOf(getTestPropertyEntity(TEST_PROPERTY_ID))
        every { getLocaleUseCase.invoke() } returns US
        coEvery { convertPriceDependingOnLocaleUseCase.invoke(BigDecimal(1000000)) } returns BigDecimal(1000000)
        every { formatPriceToHumanReadableUseCase.invoke(BigDecimal(1000000)) } returns "$1,000,000"
        coEvery { getLastUpdatedCurrencyRateDateUseCase.invoke() } coAnswers { null }
        every { formatAndRoundSurfaceToHumanReadableUseCase.invoke(BigDecimal(500)) } returns "500 m²"
        coEvery { convertToSquareFeetDependingOnLocaleUseCase.invoke(BigDecimal(500)) } returns BigDecimal(500)
        every { getStringResourceForTypeUseCase.invoke("House") } returns R.string.type_house
        every { generateMapUrlWithApiKeyUseCase.invoke(any()) } returns "https://www.google.com/maps/123456789"
        justRun { setNavigationTypeUseCase.invoke(any()) }

        viewModel = DetailViewModel(
            getCurrentPropertyUseCase = getCurrentPropertyUseCase,
            formatPriceToHumanReadableUseCase = formatPriceToHumanReadableUseCase,
            convertPriceDependingOnLocaleUseCase = convertPriceDependingOnLocaleUseCase,
            getLastUpdatedCurrencyRateDateUseCase = getLastUpdatedCurrencyRateDateUseCase,
            getLocaleUseCase = getLocaleUseCase,
            formatAndRoundSurfaceToHumanReadableUseCase = formatAndRoundSurfaceToHumanReadableUseCase,
            convertToSquareFeetDependingOnLocaleUseCase = convertToSquareFeetDependingOnLocaleUseCase,
            getStringResourceForTypeUseCase = getStringResourceForTypeUseCase,
            generateMapUrlWithApiKeyUseCase = generateMapUrlWithApiKeyUseCase,
            setNavigationTypeUseCase = setNavigationTypeUseCase,
        )
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        // When
        viewModel.viewState.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(testDetailViewStateDetails)
        }
    }

    @Test
    fun `loading case`() = testCoroutineRule.runTest {
        // Given
        every { getCurrentPropertyUseCase.invoke() } returns flowOf()

        // When
        viewModel.viewState.observeForTesting(this) {

            // Then
            assertThat(it.value).isEqualTo(testDetailViewStateLoading)
        }
    }

    @Test
    fun `on edit clicked`() {
        // When
        viewModel.onEditClicked()

        // Then
        verify(exactly = 1) { setNavigationTypeUseCase.invoke(NavigationFragmentType.EDIT_FRAGMENT) }
    }

    private val testDetailViewStateLoading = DetailViewState.LoadingState

    private val testDetailViewStateDetails = DetailViewState.PropertyDetail(
        id = TEST_PROPERTY_ID,
        propertyType = R.string.type_house,
        pictures = listOf(
            PictureBannerViewState(
                pictureUri = NativePhoto.Uri("https://www.google.com/front_view"),
                description = "Front view",
                picturePosition = 1,
                pictureNumberText = NativeText.Arguments(
                    R.string.banner_pic_number,
                    listOf("1", "3")
                ),

                ),
            PictureBannerViewState(
                pictureUri = NativePhoto.Uri("https://www.google.com/garden"),
                description = "Garden",
                picturePosition = 2,
                pictureNumberText = NativeText.Arguments(
                    R.string.banner_pic_number,
                    listOf("2", "3")
                ),

                ),
            PictureBannerViewState(
                pictureUri = NativePhoto.Uri("https://www.google.com/swimming_pool"),
                description = "Swimming pool",
                picturePosition = 3,
                pictureNumberText = NativeText.Arguments(
                    R.string.banner_pic_number,
                    listOf("3", "3")
                ),
            ),
        ),
        mapMiniature = NativePhoto.Uri("https://www.google.com/maps/123456789"),
        price = "$1,000,000",
        lastUpdatedCurrencyRateDate = NativeText.Resource(R.string.currency_rate_with_no_date_tv),
        isCurrencyLastUpdatedCurrencyRateVisible = false,
        surface = "500 m²",
        rooms = NativeText.Argument(R.string.detail_number_of_room_textview, 5),
        bathrooms = NativeText.Argument(R.string.detail_number_of_bathroom_textview, 2),
        bedrooms = NativeText.Argument(R.string.detail_number_of_bedroom_textview, 3),
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        address = NativeText.Argument(R.string.detail_location_tv, "1st, Dummy Street, 12345, Dummy City"),
        amenities = buildList<AmenityViewState.AmenityItem> {
            add(AmenityViewState.AmenityItem(R.string.amenity_school, R.drawable.baseline_school_24))
            add(AmenityViewState.AmenityItem(R.string.amenity_park, R.drawable.baseline_park_24))
            add(AmenityViewState.AmenityItem(R.string.amenity_shopping_mall, R.drawable.baseline_shopping_cart_24))
        },
        entryDate = NativeText.Argument(R.string.detail_entry_date_tv, "1/1/23"),
        agentName = NativeText.Argument(R.string.detail_manager_agent_name, "John Doe"),
        isSold = false,
        saleDate = null,
    )
}
