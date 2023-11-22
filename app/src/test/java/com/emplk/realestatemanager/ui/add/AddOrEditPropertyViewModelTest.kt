package com.emplk.realestatemanager.ui.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emplk.realestatemanager.data.property_draft.FormTypeAndTitleEntity
import com.emplk.realestatemanager.domain.agent.GetAgentsMapUseCase
import com.emplk.realestatemanager.domain.autocomplete.GetCurrentPredictionAddressesFlowWithDebounceUseCase
import com.emplk.realestatemanager.domain.autocomplete.PredictionWrapper
import com.emplk.realestatemanager.domain.connectivity.IsInternetEnabledFlowUseCase
import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.locale_formatting.currency.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.currency.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.SurfaceUnitType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.draft.GetClearPropertyFormNavigationEventAsFlowUseCase
import com.emplk.realestatemanager.domain.navigation.draft.GetDraftNavigationUseCase
import com.emplk.realestatemanager.domain.property.AddOrEditPropertyUseCase
import com.emplk.realestatemanager.domain.property.amenity.type.GetAmenityTypeUseCase
import com.emplk.realestatemanager.domain.property_draft.ResetPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.FormWithTypeEntity
import com.emplk.realestatemanager.domain.property_draft.GetFormTypeAndTitleAsFlowUseCase
import com.emplk.realestatemanager.domain.property_draft.InitPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.SetFormTitleUseCase
import com.emplk.realestatemanager.domain.property_draft.SetPropertyFormProgressUseCase
import com.emplk.realestatemanager.domain.property_draft.UpdatePropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.address.SetHasAddressFocusUseCase
import com.emplk.realestatemanager.domain.property_draft.address.SetSelectedAddressStateUseCase
import com.emplk.realestatemanager.domain.property_draft.address.UpdateOnAddressClickedUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.DeletePicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.GetPicturePreviewsAsFlowUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.SavePictureToLocalAppFilesAndToLocalDatabaseUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.UpdatePicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.id.AddAllPicturePreviewsIdsUseCase
import com.emplk.realestatemanager.domain.property_type.GetPropertyTypeUseCase
import com.emplk.realestatemanager.fixtures.getTestAgentsMap
import com.emplk.realestatemanager.fixtures.getTestPropertyTypesMap
import com.emplk.utils.TestCoroutineRule
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.math.BigDecimal

class AddOrEditPropertyViewModelTest {

    companion object {
        private const val TEST_PROPERTY_ID = 1L
    }

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val addOrEditPropertyUseCase: AddOrEditPropertyUseCase = mockk()
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase = mockk()
    private val resetPropertyFormUseCase: ResetPropertyFormUseCase = mockk()
    private val setPropertyFormProgressUseCase: SetPropertyFormProgressUseCase = mockk()
    private val updatePropertyFormUseCase: UpdatePropertyFormUseCase = mockk()
    private val updatePicturePreviewUseCase: UpdatePicturePreviewUseCase = mockk()
    private val addAllPicturePreviewsIdsUseCase: AddAllPicturePreviewsIdsUseCase = mockk()
    private val savePictureToLocalAppFilesAndToLocalDatabaseUseCase: SavePictureToLocalAppFilesAndToLocalDatabaseUseCase =
        mockk()
    private val initPropertyFormUseCase: InitPropertyFormUseCase = mockk()
    private val getPicturePreviewsAsFlowUseCase: GetPicturePreviewsAsFlowUseCase = mockk()
    private val deletePicturePreviewUseCase: DeletePicturePreviewUseCase = mockk()
    private val getAgentsMapUseCase: GetAgentsMapUseCase = mockk()
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase = mockk()
    private val convertPriceDependingOnLocaleUseCase: ConvertPriceDependingOnLocaleUseCase = mockk()
    private val convertToSquareFeetDependingOnLocaleUseCase: ConvertToSquareFeetDependingOnLocaleUseCase = mockk()
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase = mockk()
    private val getFormTitleUseCase: GetFormTypeAndTitleAsFlowUseCase = mockk()
    private val setFormTitleUseCase: SetFormTitleUseCase = mockk()
    private val setSelectedAddressStateUseCase: SetSelectedAddressStateUseCase = mockk()
    private val updateOnAddressClickedUseCase: UpdateOnAddressClickedUseCase = mockk()
    private val setHasAddressFocusUseCase: SetHasAddressFocusUseCase = mockk()
    private val getPropertyTypeUseCase: GetPropertyTypeUseCase = mockk()
    private val getAmenityTypeUseCase: GetAmenityTypeUseCase = mockk()
    private val getCurrentPredictionAddressesFlowWithDebounceUseCase: GetCurrentPredictionAddressesFlowWithDebounceUseCase =
        mockk()
    private val isInternetEnabledFlowUseCase: IsInternetEnabledFlowUseCase = mockk()
    private val getDraftNavigationUseCase: GetDraftNavigationUseCase = mockk()
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase = mockk()
    private val getClearPropertyFormNavigationEventAsFlowUseCase: GetClearPropertyFormNavigationEventAsFlowUseCase =
        mockk()

    private lateinit var addOrEditPropertyViewModel: AddOrEditPropertyViewModel

    @Before
    fun setUp() {

        coJustRun { addOrEditPropertyUseCase.invoke(any()) }
        coEvery { getCurrentPropertyIdFlowUseCase.invoke() } returns flowOf(TEST_PROPERTY_ID)
        coJustRun { resetPropertyFormUseCase.invoke(any()) }
        justRun { setPropertyFormProgressUseCase.invoke(any()) }
        coJustRun { updatePropertyFormUseCase.invoke(any()) }
        coEvery { updatePicturePreviewUseCase.invoke(any(), any(), any()) }
        justRun { addAllPicturePreviewsIdsUseCase.invoke(any()) }
        coEvery { savePictureToLocalAppFilesAndToLocalDatabaseUseCase.invoke(any(), any(), any()) } returns 1L
        coEvery { initPropertyFormUseCase.invoke(any()) } returns testFormTypeEntity
        coEvery { getPicturePreviewsAsFlowUseCase.invoke(any()) } returns flowOf(listOf())
        coJustRun { deletePicturePreviewUseCase.invoke(any(), any()) }
        every { getAgentsMapUseCase.invoke() } returns getTestAgentsMap()
        every { getCurrencyTypeUseCase.invoke() } returns CurrencyType.DOLLAR
        coEvery { convertPriceDependingOnLocaleUseCase.invoke(any()) } returns BigDecimal(1000000)
        every { convertToSquareFeetDependingOnLocaleUseCase.invoke(any()) } returns BigDecimal(500)
        every { getSurfaceUnitUseCase.invoke() } returns SurfaceUnitType.SQUARE_FOOT
        coEvery { getFormTitleUseCase.invoke() } returns flowOf(
            FormTypeAndTitleEntity(
                formType = FormType.ADD,
                title = "Test property"
            )
        )
        coJustRun { updateOnAddressClickedUseCase.invoke(any(), any()) }
        justRun { setHasAddressFocusUseCase.invoke(any()) }
        every { getPropertyTypeUseCase.invoke() } returns getTestPropertyTypesMap()
        coEvery { getCurrentPredictionAddressesFlowWithDebounceUseCase.invoke() } returns flowOf(
            testPredictionSuccessWrapper
        )
        coEvery { isInternetEnabledFlowUseCase.invoke() } returns flowOf(true)
        coEvery { getDraftNavigationUseCase.invoke() } returns emptyFlow()
        justRun { setNavigationTypeUseCase.invoke(any()) }
        coEvery { getClearPropertyFormNavigationEventAsFlowUseCase.invoke() } returns emptyFlow()

        addOrEditPropertyViewModel = AddOrEditPropertyViewModel(
            addOrEditPropertyUseCase,
            initPropertyFormUseCase,
            getCurrentPropertyIdFlowUseCase,
            resetPropertyFormUseCase,
            setPropertyFormProgressUseCase,
            updatePropertyFormUseCase,
            updatePicturePreviewUseCase,
            addAllPicturePreviewsIdsUseCase,
            savePictureToLocalAppFilesAndToLocalDatabaseUseCase,
            getPicturePreviewsAsFlowUseCase,
            deletePicturePreviewUseCase,
            getAgentsMapUseCase,
            getCurrencyTypeUseCase,
            convertPriceDependingOnLocaleUseCase,
            convertToSquareFeetDependingOnLocaleUseCase,
            getSurfaceUnitUseCase,
            getFormTitleUseCase,
            setFormTitleUseCase,
            setSelectedAddressStateUseCase,
            updateOnAddressClickedUseCase,
            setHasAddressFocusUseCase,
            getPropertyTypeUseCase,
            getAmenityTypeUseCase,
            getCurrentPredictionAddressesFlowWithDebounceUseCase,
            isInternetEnabledFlowUseCase,
            getDraftNavigationUseCase,
            setNavigationTypeUseCase,
            getClearPropertyFormNavigationEventAsFlowUseCase
        )
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {

    }

    private val testFormTypeEntity = FormWithTypeEntity(
        formDraftEntity = mockk(),
        formType = mockk()
    )

    private val testPredictionSuccessWrapper = PredictionWrapper.Success(
        listOf(
            "1st, Dummy Street, 12345, Dummy City",
            "2nd, Dummy Street, 12345, Dummy City",
            "3rd, Dummy Street, 12345, Dummy City",
            "4th, Dummy Street, 12345, Dummy City",
            "5th, Dummy Street, 12345, Dummy City",
        )
    )
}