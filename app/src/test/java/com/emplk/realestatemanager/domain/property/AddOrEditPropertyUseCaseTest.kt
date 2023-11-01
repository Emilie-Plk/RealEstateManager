package com.emplk.realestatemanager.domain.property

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import com.emplk.realestatemanager.domain.current_property.ResetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.geocoding.GeocodingRepository
import com.emplk.realestatemanager.domain.geocoding.GeocodingWrapper
import com.emplk.realestatemanager.domain.locale_formatting.ConvertSurfaceToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.ConvertToUsdDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.map_picture.GenerateMapBaseUrlWithParamsUseCase
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.pictures.PictureRepository
import com.emplk.realestatemanager.domain.property_draft.ClearPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.FormDraftRepository
import com.emplk.realestatemanager.domain.property_draft.UpdatePropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.GetPicturePreviewsUseCase
import com.emplk.realestatemanager.fixtures.getTestFormDraftParams
import com.emplk.realestatemanager.ui.add.FormEvent
import com.emplk.utils.TestCoroutineRule
import com.google.android.gms.maps.model.LatLng
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset

class AddOrEditPropertyUseCaseTest {

    companion object {
        private const val TEST_PROPERTY_ID = 1L
        private val TEST_GEOCODING_WRAPPER_SUCCESS = GeocodingWrapper.Success(
            LatLng(123.0, 456.0)
        )
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val propertyRepository: PropertyRepository = mockk()
    private val formDraftRepository: FormDraftRepository = mockk()
    private val geocodingRepository: GeocodingRepository = mockk()
    private val pictureRepository: PictureRepository = mockk()
    private val generateMapBaseUrlWithParamsUseCase: GenerateMapBaseUrlWithParamsUseCase = mockk()
    private val convertToUsdDependingOnLocaleUseCase: ConvertToUsdDependingOnLocaleUseCase = mockk()
    private val getPicturePreviewsUseCase: GetPicturePreviewsUseCase = mockk()
    private val convertSurfaceToSquareFeetDependingOnLocaleUseCase: ConvertSurfaceToSquareFeetDependingOnLocaleUseCase =
        mockk()
    private val updatePropertyFormUseCase: UpdatePropertyFormUseCase = mockk()
    private val clearPropertyFormUseCase: ClearPropertyFormUseCase = mockk()
    private val resetCurrentPropertyIdUseCase: ResetCurrentPropertyIdUseCase = mockk()
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase = mockk()

    private val addOrEditPropertyUseCase = AddOrEditPropertyUseCase(
        propertyRepository,
        formDraftRepository,
        geocodingRepository,
        pictureRepository,
        generateMapBaseUrlWithParamsUseCase,
        convertToUsdDependingOnLocaleUseCase,
        getPicturePreviewsUseCase,
        convertSurfaceToSquareFeetDependingOnLocaleUseCase,
        updatePropertyFormUseCase,
        clearPropertyFormUseCase,
        resetCurrentPropertyIdUseCase,
        setNavigationTypeUseCase,
        Clock.fixed(Instant.ofEpochSecond(1698758555), ZoneOffset.UTC) // Tue, 31 Oct 2023 13:22:35 GMT
    )

    @Before
    fun setUp() {
        coEvery { propertyRepository.update(any()) } returns true

        coEvery { propertyRepository.addPropertyWithDetails(any()) } returns true

        coEvery { geocodingRepository.getLatLong(any()) } returns TEST_GEOCODING_WRAPPER_SUCCESS

        coEvery { convertToUsdDependingOnLocaleUseCase.invoke(any()) } returns BigDecimal(1000000)

        every { convertSurfaceToSquareFeetDependingOnLocaleUseCase.invoke(any()) } returns BigDecimal(500)

        coEvery { pictureRepository.getPicturesIds(TEST_PROPERTY_ID) } returns listOf(1L, 2L, 3L)

        coEvery { getPicturePreviewsUseCase.invoke(any()) } returns emptyList()

        coEvery { pictureRepository.delete(TEST_PROPERTY_ID) }  // pa sûre lol

        every { resetCurrentPropertyIdUseCase.invoke() } returns Unit

        coEvery { clearPropertyFormUseCase.invoke(any()) } returns Unit

        every { setNavigationTypeUseCase.invoke(any()) } returns Unit

        every { generateMapBaseUrlWithParamsUseCase.invoke(any()) } returns "https://www.google.com/maps/123456789"
    }

    @Test
    fun `invoke (update) - nominal case`() = testCoroutineRule.runTest {
        // Given
        coEvery { formDraftRepository.doesPropertyExist(TEST_PROPERTY_ID) } returns true

        // When
        val result = addOrEditPropertyUseCase.invoke(getTestFormDraftParams(TEST_PROPERTY_ID))

        // Then
        assertThat { result is FormEvent.Toast }
    }

    @Test
    fun `invoke (add) - nominal case`() = testCoroutineRule.runTest {
        // Given
        coEvery { formDraftRepository.doesPropertyExist(TEST_PROPERTY_ID) } returns false

        // When
        val result = addOrEditPropertyUseCase.invoke(getTestFormDraftParams(TEST_PROPERTY_ID))

        // Then
        assertThat { result is FormEvent.Toast }
    }

    @Test
    fun `edge case - `() = testCoroutineRule.runTest {
        val formDraftParams = getTestFormDraftParams(TEST_PROPERTY_ID)
        val copy = formDraftParams.copy(
            address = null,
            price = BigDecimal.ZERO
        )
        assertThrows(IllegalArgumentException::class.java) { coEvery { addOrEditPropertyUseCase.invoke(copy) } }
    }
}