package com.emplk.realestatemanager.ui.add

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.emplk.realestatemanager.R
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
import com.emplk.realestatemanager.domain.navigation.draft.IsFormCompletedAsFlowUseCase
import com.emplk.realestatemanager.domain.navigation.draft.IsPropertyInsertingInDatabaseFlowUseCase
import com.emplk.realestatemanager.domain.navigation.draft.SetFormCompletionUseCase
import com.emplk.realestatemanager.domain.property.AddOrEditPropertyUseCase
import com.emplk.realestatemanager.domain.property.amenity.type.GetAmenityTypeUseCase
import com.emplk.realestatemanager.domain.property_draft.FormDraftEntity
import com.emplk.realestatemanager.domain.property_draft.FormWithTypeEntity
import com.emplk.realestatemanager.domain.property_draft.GetFormTypeAndTitleAsFlowUseCase
import com.emplk.realestatemanager.domain.property_draft.InitPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.ResetPropertyFormUseCase
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
import com.emplk.realestatemanager.fixtures.getTestAmenities
import com.emplk.realestatemanager.fixtures.getTestFormDraftParams
import com.emplk.realestatemanager.fixtures.getTestPropertyTypesMap
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionViewState
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentViewStateItem
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.add.type.PropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativeText
import com.emplk.utils.TestCoroutineRule
import com.emplk.utils.observeForTesting
import io.mockk.coEvery
import io.mockk.coJustRun
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runCurrent
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
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
    private val initPropertyFormUseCase: InitPropertyFormUseCase = mockk()
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase = mockk()
    private val resetPropertyFormUseCase: ResetPropertyFormUseCase = mockk()
    private val setPropertyFormProgressUseCase: SetPropertyFormProgressUseCase = mockk()
    private val updatePropertyFormUseCase: UpdatePropertyFormUseCase = mockk()
    private val isFormCompletedAsFlowUseCase: IsFormCompletedAsFlowUseCase = mockk()
    private val setFormCompletionUseCase: SetFormCompletionUseCase = mockk()
    private val isPropertyInsertingInDatabaseFlowUseCase: IsPropertyInsertingInDatabaseFlowUseCase = mockk()
    private val updatePicturePreviewUseCase: UpdatePicturePreviewUseCase = mockk()
    private val addAllPicturePreviewsIdsUseCase: AddAllPicturePreviewsIdsUseCase = mockk()
    private val savePictureToLocalAppFilesAndToLocalDatabaseUseCase: SavePictureToLocalAppFilesAndToLocalDatabaseUseCase =
        mockk()
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
    private val getClearPropertyFormNavigationEventAsFlowUseCase: GetClearPropertyFormNavigationEventAsFlowUseCase =
        mockk()

    private lateinit var addOrEditPropertyViewModel: AddOrEditPropertyViewModel

    @Before
    fun setUp() {
        coJustRun { addOrEditPropertyUseCase.invoke(any()) }
        coEvery { initPropertyFormUseCase.invoke(TEST_PROPERTY_ID) } returns testFormTypeEntity
        coEvery { getCurrentPropertyIdFlowUseCase.invoke() } returns flowOf(TEST_PROPERTY_ID)
        coJustRun { resetPropertyFormUseCase.invoke(any()) }
        justRun { setPropertyFormProgressUseCase.invoke(any()) }
        coJustRun { updatePropertyFormUseCase.invoke(any()) }
        coEvery { isFormCompletedAsFlowUseCase.invoke() } returns flowOf(false)
        justRun { setFormCompletionUseCase.invoke(false) }
        coEvery { isPropertyInsertingInDatabaseFlowUseCase.invoke() } returns flowOf(false)
        coEvery { updatePicturePreviewUseCase.invoke(any(), any(), any()) }
        justRun { addAllPicturePreviewsIdsUseCase.invoke(any()) }
        coEvery { savePictureToLocalAppFilesAndToLocalDatabaseUseCase.invoke(any(), any(), any()) } returns 1L
        coEvery { getPicturePreviewsAsFlowUseCase.invoke(any()) } returns flowOf(listOf())
        coJustRun { deletePicturePreviewUseCase.invoke(any(), any()) }
        every { getAgentsMapUseCase.invoke() } returns getTestAgentsMap()
        every { getCurrencyTypeUseCase.invoke() } returns CurrencyType.DOLLAR
        coEvery { convertPriceDependingOnLocaleUseCase.invoke(any()) } returns BigDecimal.ZERO
        every { convertToSquareFeetDependingOnLocaleUseCase.invoke(any()) } returns BigDecimal.ZERO
        every { getSurfaceUnitUseCase.invoke() } returns SurfaceUnitType.SQUARE_FOOT
        justRun { setFormTitleUseCase.invoke(FormType.ADD, null) }
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
        every { getAmenityTypeUseCase.invoke() } returns getTestAmenities()
        coEvery { isInternetEnabledFlowUseCase.invoke() } returns flowOf(true)
        coEvery { getDraftNavigationUseCase.invoke() } returns emptyFlow()
        coEvery { getClearPropertyFormNavigationEventAsFlowUseCase.invoke() } returns emptyFlow()

        addOrEditPropertyViewModel = AddOrEditPropertyViewModel(
            addOrEditPropertyUseCase,
            initPropertyFormUseCase,
            getCurrentPropertyIdFlowUseCase,
            resetPropertyFormUseCase,
            setPropertyFormProgressUseCase,
            updatePropertyFormUseCase,
            isFormCompletedAsFlowUseCase,
            setFormCompletionUseCase,
            isPropertyInsertingInDatabaseFlowUseCase,
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
            getClearPropertyFormNavigationEventAsFlowUseCase
        )
    }

    @Test
    fun `initial case`() = testCoroutineRule.runTest {
        addOrEditPropertyViewModel.viewStateLiveData.observeForTesting(this) {
            assertEquals(testPropertyFormViewState, it.value)
        }
    }

    @Test
    fun `loading case`() = testCoroutineRule.runTest {
        // When
        coEvery { isFormCompletedAsFlowUseCase.invoke() } returns emptyFlow()

        addOrEditPropertyViewModel.viewStateLiveData.observeForTesting(this) {
            // Then
            assertEquals(testPropertyFormViewStateLoading, it.value)
        }
    }
/*
    @Test
    fun `on add property clicked - triggers on submit button event`() = testCoroutineRule.runTest {
        // Given
        coEvery { addOrEditPropertyUseCase.invoke(getTestFormDraftParams(TEST_PROPERTY_ID)) } returns FormEvent.Toast(
            NativeText.Simple("Test")
        )

        addOrEditPropertyViewModel.viewEventLiveData.observeForTesting(this) {
            // When
            addOrEditPropertyViewModel.onAddPropertyClicked()
            runCurrent()

            // Then
            assertEquals(Event(FormEvent.Toast(NativeText.Simple("Test"))), it.value)
        }
    }*/

    @Test
    fun `when clicking on address selection - defined it as selected address`() = testCoroutineRule.runTest {
        addOrEditPropertyViewModel.viewStateLiveData.observeForTesting(this) {
            // When
            ((it.value as PropertyFormViewState.PropertyForm)
                .addressPredictions[2] as PredictionViewState.Prediction).onClickEvent.invoke(
                testPredictionSuccessWrapper.predictions[2]
            )
            runCurrent()

            // Then
            assertEquals(
                testPredictionSuccessWrapper.predictions[2],
                (it.value as PropertyFormViewState.PropertyForm).address
            )
            assertTrue((it.value as PropertyFormViewState.PropertyForm).isAddressValid)
        }
    }

    @Test
    fun `on agent changed - agent is set`() = testCoroutineRule.runTest {
        addOrEditPropertyViewModel.viewStateLiveData.observeForTesting(this) {
            // When
            addOrEditPropertyViewModel.onAgentSelected(getTestAgentsMap().values.toList()[1])
            runCurrent()

            // Then
            assertEquals(
                getTestAgentsMap().values.toList()[1],
                (it.value as PropertyFormViewState.PropertyForm).selectedAgent
            )
        }
    }

 /*   @Test
    fun `on description changed - description is updated`() = testCoroutineRule.runTest {
        addOrEditPropertyViewModel.viewStateLiveData.observeForTesting(this) {
            // When
            addOrEditPropertyViewModel.onDescriptionChanged("Test description")
            runCurrent()

            // Then
            assertEquals(
                getTestAgentsMap().values.toList()[1],
                (it.value as PropertyFormViewState.PropertyForm).selectedAgent
            )
        }
    }*/

    @Test
    fun `on number of rooms changed - number of rooms is updated`() = testCoroutineRule.runTest {
        addOrEditPropertyViewModel.viewStateLiveData.observeForTesting(this) {
            // When
            addOrEditPropertyViewModel.onRoomsNumberChanged(10)
            runCurrent()

            // Then
            assertEquals(10, (it.value as PropertyFormViewState.PropertyForm).nbRooms)
        }
    }

    @Test
    fun `on number of bedrooms changed - number of bedrooms is updated`() = testCoroutineRule.runTest {
        addOrEditPropertyViewModel.viewStateLiveData.observeForTesting(this) {
            // When
            addOrEditPropertyViewModel.onBedroomsNumberChanged(4)
            runCurrent()

            // Then
            assertEquals(4, (it.value as PropertyFormViewState.PropertyForm).nbBedrooms)
        }
    }

    @Test
    fun `on number of bathrooms changed - number of bathrooms is updated`() = testCoroutineRule.runTest {
        addOrEditPropertyViewModel.viewStateLiveData.observeForTesting(this) {
            // When
            addOrEditPropertyViewModel.onBathroomsNumberChanged(6)
            runCurrent()

            // Then
            assertEquals(6, (it.value as PropertyFormViewState.PropertyForm).nbBathrooms)
        }
    }


    @Test
    fun `when form is completed - submit button is true`() = testCoroutineRule.runTest {
        // Given
        coEvery { isFormCompletedAsFlowUseCase.invoke() } returns flowOf(true)

        addOrEditPropertyViewModel.viewStateLiveData.observeForTesting(this) {
            // Then
            assertTrue((it.value as PropertyFormViewState.PropertyForm).isSubmitButtonEnabled)
        }
    }

    @Test
    fun `when is adding in database - progress bar is true`() = testCoroutineRule.runTest {
        // Given
        coEvery { isPropertyInsertingInDatabaseFlowUseCase.invoke() } returns flowOf(true)

        addOrEditPropertyViewModel.viewStateLiveData.observeForTesting(this) {
            // Then
            assertTrue((it.value as PropertyFormViewState.PropertyForm).isProgressBarVisible)
        }
    }


    private val testFormTypeEntity = FormWithTypeEntity(
        formDraftEntity = FormDraftEntity(
            id = 0L,
            type = null,
            title = null,
            price = BigDecimal.ZERO,
            surface = BigDecimal.ZERO,
            rooms = 0,
            bedrooms = 0,
            bathrooms = 0,
            description = "",
            amenities = emptyList(),
            address = null,
            isAddressValid = false,
            agentName = null,
            isSold = false,
            entryDate = null,
            saleDate = null,
            lastEditionDate = null,
        ),
        formType = FormType.ADD,
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

    private val testPropertyFormViewStateLoading = PropertyFormViewState.LoadingState

    private val testPropertyFormViewState = PropertyFormViewState.PropertyForm(
        propertyType = null,
        addressPredictions = listOf(
            PredictionViewState.Prediction(
                address = "1st, Dummy Street, 12345, Dummy City",
                onClickEvent = EquatableCallbackWithParam { }
            ),
            PredictionViewState.Prediction(
                address = "2nd, Dummy Street, 12345, Dummy City",
                onClickEvent = EquatableCallbackWithParam { }
            ),
            PredictionViewState.Prediction(
                address = "3rd, Dummy Street, 12345, Dummy City",
                onClickEvent = EquatableCallbackWithParam { }
            ),
            PredictionViewState.Prediction(
                address = "4th, Dummy Street, 12345, Dummy City",
                onClickEvent = EquatableCallbackWithParam { }
            ),
            PredictionViewState.Prediction(
                address = "5th, Dummy Street, 12345, Dummy City",
                onClickEvent = EquatableCallbackWithParam { }
            ),
        ),
        isAddressValid = false,
        address = null,
        price = "",
        surface = "",
        description = "",
        nbRooms = 0,
        nbBathrooms = 0,
        nbBedrooms = 0,
        pictures = listOf(),
        agents = getTestAgentsMap().map { AddPropertyAgentViewStateItem(it.key, it.value) },
        selectedAgent = null,
        priceCurrencyHint = NativeText.Resource(R.string.price_in_dollar),
        currencyDrawableRes = R.drawable.baseline_dollar_24,
        surfaceUnit = NativeText.Argument(
            R.string.surface_area_unit_in_n,
            "sq ft",
        ),
        propertyCreationDate = null,
        isSubmitButtonEnabled = false,
        submitButtonText = NativeText.Resource(R.string.form_create_button),
        isProgressBarVisible = false,
        amenities = getTestAmenities().map {
            AmenityViewState.AmenityCheckbox(
                id = it.id,
                name = it.name,
                isChecked = false,
                onCheckBoxClicked = EquatableCallbackWithParam { },
                iconDrawable = it.iconDrawable,
                stringRes = it.stringRes,
            )
        },
        propertyTypes = getTestPropertyTypesMap().map { PropertyTypeViewStateItem(it.key, it.value) },
        isSold = false,
        soldDate = null,
        areEditItemsVisible = false,
        isInternetEnabled = true,
    )
}