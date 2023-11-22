package com.emplk.realestatemanager.domain.property

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import assertk.assertThat
import com.emplk.realestatemanager.domain.current_property.ResetCurrentPropertyIdUseCase
import com.emplk.realestatemanager.domain.geocoding.GeocodingRepository
import com.emplk.realestatemanager.domain.geocoding.GeocodingWrapper
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertSurfaceToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToUsdDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.map_picture.GenerateMapBaseUrlWithParamsUseCase
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.pictures.PictureRepository
import com.emplk.realestatemanager.domain.property_draft.ResetPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.FormDraftRepository
import com.emplk.realestatemanager.domain.property_draft.UpdatePropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.GetPicturePreviewsUseCase
import com.emplk.realestatemanager.fixtures.getTestFormDraftParams
import com.emplk.realestatemanager.fixtures.testFixedClock
import com.emplk.realestatemanager.ui.add.FormEvent
import com.emplk.utils.TestCoroutineRule
import com.google.android.gms.maps.model.LatLng
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

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
    private val resetPropertyFormUseCase: ResetPropertyFormUseCase = mockk()
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
        resetPropertyFormUseCase,
        resetCurrentPropertyIdUseCase,
        setNavigationTypeUseCase,
        testFixedClock
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

        coEvery { pictureRepository.delete(TEST_PROPERTY_ID) }

        justRun { resetCurrentPropertyIdUseCase.invoke() }

        coJustRun { resetPropertyFormUseCase.invoke(any()) }

        justRun { setNavigationTypeUseCase.invoke(any()) }

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
    fun `edge case - incorrect FormDraftParams properties throws IllegalArgumentException`() =
        testCoroutineRule.runTest {
            val formDraftParams = getTestFormDraftParams(TEST_PROPERTY_ID)
            val copy = formDraftParams.copy(
                address = null,
                price = BigDecimal.ZERO
            )
            assertThrows(IllegalArgumentException::class.java) { coEvery { addOrEditPropertyUseCase.invoke(copy) } }
        }
}