package com.emplk.realestatemanager.ui.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.agent.GetRealEstateAgentsUseCase
import com.emplk.realestatemanager.domain.autocomplete.GetCurrentPredictionAddressesFlowWithDebounceUseCase
import com.emplk.realestatemanager.domain.autocomplete.PredictionWrapper
import com.emplk.realestatemanager.domain.connectivity.IsInternetEnabledFlowUseCase
import com.emplk.realestatemanager.domain.currency_rate.ConvertPriceDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.locale_formatting.currency.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.ConvertToSquareFeetDependingOnLocaleUseCase
import com.emplk.realestatemanager.domain.locale_formatting.surface.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.navigation.draft.GetClearPropertyFormNavigationEventAsFlowUseCase
import com.emplk.realestatemanager.domain.navigation.draft.GetSavePropertyDraftEventUseCase
import com.emplk.realestatemanager.domain.navigation.draft.IsFormCompletedAsFlowUseCase
import com.emplk.realestatemanager.domain.navigation.draft.IsPropertyInsertingInDatabaseFlowUseCase
import com.emplk.realestatemanager.domain.navigation.draft.SetFormCompletionUseCase
import com.emplk.realestatemanager.domain.property.AddOrEditPropertyUseCase
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property.amenity.type.GetAmenityTypeUseCase
import com.emplk.realestatemanager.domain.property_draft.FormDraftParams
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
import com.emplk.realestatemanager.domain.property_draft.picture_preview.PicturePreviewEntity
import com.emplk.realestatemanager.domain.property_draft.picture_preview.SavePictureToLocalAppFilesAndToLocalDatabaseUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.UpdatePicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.id.AddAllPicturePreviewsIdsUseCase
import com.emplk.realestatemanager.domain.property_type.GetPropertyTypeUseCase
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionViewState
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentViewStateItem
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.add.picture_preview.PicturePreviewStateItem
import com.emplk.realestatemanager.ui.add.type.PropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativeText
import com.emplk.realestatemanager.ui.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import javax.inject.Inject
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class AddOrEditPropertyViewModel @Inject constructor(
    private val addOrEditPropertyUseCase: AddOrEditPropertyUseCase,
    private val initPropertyFormUseCase: InitPropertyFormUseCase,
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase,
    private val resetPropertyFormUseCase: ResetPropertyFormUseCase,
    private val setPropertyFormProgressUseCase: SetPropertyFormProgressUseCase,
    private val updatePropertyFormUseCase: UpdatePropertyFormUseCase,
    private val isFormCompletedAsFlowUseCase: IsFormCompletedAsFlowUseCase,
    private val setFormCompletionUseCase: SetFormCompletionUseCase,
    private val isPropertyInsertingInDatabaseFlowUseCase: IsPropertyInsertingInDatabaseFlowUseCase,
    private val updatePicturePreviewUseCase: UpdatePicturePreviewUseCase,
    private val addAllPicturePreviewsIdsUseCase: AddAllPicturePreviewsIdsUseCase,
    private val savePictureToLocalAppFilesAndToLocalDatabaseUseCase: SavePictureToLocalAppFilesAndToLocalDatabaseUseCase,
    private val getPicturePreviewsAsFlowUseCase: GetPicturePreviewsAsFlowUseCase,
    private val deletePicturePreviewUseCase: DeletePicturePreviewUseCase,
    private val getRealEstateAgents: GetRealEstateAgentsUseCase,
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase,
    private val convertPriceDependingOnLocaleUseCase: ConvertPriceDependingOnLocaleUseCase,
    private val convertToSquareFeetDependingOnLocaleUseCase: ConvertToSquareFeetDependingOnLocaleUseCase,
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
    private val getFormTypeAndTitleAsFlowUseCase: GetFormTypeAndTitleAsFlowUseCase,
    private val setFormTitleUseCase: SetFormTitleUseCase,
    private val setSelectedAddressStateUseCase: SetSelectedAddressStateUseCase,
    private val updateOnAddressClickedUseCase: UpdateOnAddressClickedUseCase,
    private val setHasAddressFocusUseCase: SetHasAddressFocusUseCase,
    private val getPropertyTypeUseCase: GetPropertyTypeUseCase,
    private val getAmenityTypeUseCase: GetAmenityTypeUseCase,
    private val getCurrentPredictionAddressesFlowWithDebounceUseCase: GetCurrentPredictionAddressesFlowWithDebounceUseCase,
    private val isInternetEnabledFlowUseCase: IsInternetEnabledFlowUseCase,
    private val getSavePropertyDraftEventUseCase: GetSavePropertyDraftEventUseCase,
    private val getClearPropertyFormNavigationEventAsFlowUseCase: GetClearPropertyFormNavigationEventAsFlowUseCase,
) : ViewModel() {

    private val formMutableStateFlow = MutableStateFlow(FormDraftParams())
    private val onSubmitButtonClickedMutableSharedFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val isLoadingMutableStateFlow = MutableStateFlow(true)

    val viewEventLiveData: LiveData<Event<FormEvent>> = liveData {
        coroutineScope {
            launch {
                isLoadingMutableStateFlow.collect { isLoading ->
                    if (isLoading) emit(
                        Event(
                            FormEvent.Loading
                        )
                    ) else emit(
                        Event(
                            FormEvent.Form
                        )
                    )
                }
            }

            launch {
                onSubmitButtonClickedMutableSharedFlow.collectLatest {
                    emit(
                        Event(
                            addOrEditPropertyUseCase.invoke(formMutableStateFlow.value)
                        )
                    )
                }
            }
        }
    }

    val viewStateLiveData: LiveData<PropertyFormViewState> = liveData {
        coroutineScope {
            if (latestValue == null) {
                isLoadingMutableStateFlow.tryEmit(true)
                delay(400)
            }

            val propertyId = getCurrentPropertyIdFlowUseCase.invoke().firstOrNull()
            val formWithType = initPropertyFormUseCase.invoke(propertyId)

            Log.d("AddVM", "formWithType: $formWithType with id: ${formWithType.formDraftEntity.id}")

            formMutableStateFlow.update { form ->
                form.copy(
                    id = formWithType.formDraftEntity.id,
                    formType = formWithType.formType,
                    typeDatabaseName = formWithType.formDraftEntity.type,
                    draftTitle = formWithType.formDraftEntity.title,
                    address = formWithType.formDraftEntity.address,
                    isAddressValid = formWithType.formDraftEntity.isAddressValid,
                    price = convertPriceDependingOnLocaleUseCase.invoke(formWithType.formDraftEntity.price),
                    surface = convertToSquareFeetDependingOnLocaleUseCase.invoke(formWithType.formDraftEntity.surface),
                    description = formWithType.formDraftEntity.description,
                    nbRooms = formWithType.formDraftEntity.rooms ?: 0,
                    nbBathrooms = formWithType.formDraftEntity.bathrooms ?: 0,
                    nbBedrooms = formWithType.formDraftEntity.bedrooms ?: 0,
                    agent = formWithType.formDraftEntity.agentName,
                    selectedAmenities = formWithType.formDraftEntity.amenities,
                    pictureIds = formWithType.formDraftEntity.pictures.map { it.id },
                    featuredPictureId = formWithType.formDraftEntity.pictures.find { it.isFeatured }?.id,
                    entryDate = formWithType.formDraftEntity.entryDate,
                    lastEditionDate = formWithType.formDraftEntity.lastEditionDate,
                    isSold = formWithType.formDraftEntity.saleDate != null,
                    soldDate = formWithType.formDraftEntity.saleDate,
                )
            }

            addAllPicturePreviewsIdsUseCase.invoke(formMutableStateFlow.value.pictureIds)

            setFormTitleUseCase.invoke(formWithType.formType, formWithType.formDraftEntity.title)

            launch {
                combine(
                    formMutableStateFlow,
                    getPicturePreviewsAsFlowUseCase.invoke(formMutableStateFlow.value.id),
                    getCurrentPredictionAddressesFlowWithDebounceUseCase.invoke()
                        .onStart { emit(null) },
                    isPropertyInsertingInDatabaseFlowUseCase.invoke().onStart { emit(null) },
                    isFormCompletedAsFlowUseCase.invoke(),
                    isInternetEnabledFlowUseCase.invoke(),
                ) { form, picturePreviews, predictionWrapper, isAddingInDatabase, isFormCompleted, isInternetEnabled ->

                    isLoadingMutableStateFlow.tryEmit(false)

                    val currencyType = getCurrencyTypeUseCase.invoke()
                    val amenityTypes = getAmenityTypeUseCase.invoke()
                    val propertyTypes = getPropertyTypeUseCase.invoke()
                    val agents = getRealEstateAgents.invoke()

                    setPropertyFormProgressUseCase.invoke(
                        !form.typeDatabaseName.isNullOrBlank() ||
                                !form.address.isNullOrBlank() ||
                                form.price > BigDecimal.ZERO ||
                                form.surface > BigDecimal.ZERO ||
                                !form.description.isNullOrBlank() ||
                                form.nbRooms > 0 ||
                                form.nbBathrooms > 0 ||
                                form.nbBedrooms > 0 ||
                                !form.agent.isNullOrBlank() ||
                                form.selectedAmenities.isNotEmpty() ||
                                form.pictureIds.isNotEmpty() ||
                                form.featuredPictureId != null
                    )

                    setFormCompletionUseCase.invoke(
                        form.typeDatabaseName != null &&
                                !form.address.isNullOrBlank() &&
                                (isInternetEnabled && form.isAddressValid || !isInternetEnabled) &&
                                form.price > BigDecimal.ZERO &&
                                form.surface > BigDecimal.ZERO &&
                                !form.description.isNullOrBlank() &&
                                form.nbRooms > 0 &&
                                form.nbBathrooms > 0 &&
                                form.nbBedrooms > 0 &&
                                !form.agent.isNullOrBlank() &&
                                form.selectedAmenities.isNotEmpty() &&
                                form.pictureIds.isNotEmpty() &&
                                form.featuredPictureId != null
                    )

                    PropertyFormViewState(
                        propertyType = propertyTypes.find { it.databaseName == form.typeDatabaseName }?.stringRes,
                        address = form.address,
                        price = if (form.price == BigDecimal.ZERO) "" else form.price.toString(),
                        surface = if (form.surface == BigDecimal.ZERO) "" else form.surface.toString(),
                        description = form.description,
                        nbRooms = form.nbRooms,
                        nbBathrooms = form.nbBathrooms,
                        nbBedrooms = form.nbBedrooms,
                        pictures = mapPicturePreviews(picturePreviews, form),
                        selectedAgent = form.agent,
                        priceCurrencyHint = NativeText.Argument(
                            R.string.price_hint,
                            currencyType.symbol,
                        ),
                        currencyDrawableRes = currencyType.drawableRes,
                        surfaceUnit = NativeText.Argument(
                            R.string.surface_area_unit_in_n,
                            getSurfaceUnitUseCase.invoke().symbol,
                        ),
                        propertyCreationDate = if (form.formType == FormType.EDIT) {
                            form.entryDate?.let {
                                NativeText.Argument(
                                    R.string.form_creation_date_tv,
                                    it.format(
                                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                                    )
                                )
                            }
                        } else null,
                        isSubmitButtonEnabled = isFormCompleted ?: false,
                        submitButtonText = if (form.formType == FormType.ADD) NativeText.Resource(R.string.form_create_button)
                        else NativeText.Resource(R.string.form_edit_button),
                        isProgressBarVisible = isAddingInDatabase == true,
                        amenities = mapAmenityTypesToViewStates(amenityTypes),
                        propertyTypes = propertyTypes
                            .filterNot { it.id == 0L }
                            .map { propertyType ->
                                PropertyTypeViewStateItem(
                                    id = propertyType.id,
                                    name = NativeText.Resource(propertyType.stringRes),
                                    databaseName = propertyType.databaseName
                                )
                            },
                        agents = agents.map { agent ->
                            AddPropertyAgentViewStateItem(
                                id = agent.id,
                                name = agent.agentName,
                            )
                        },
                        addressPredictions = mapPredictionsToViewState(predictionWrapper, isInternetEnabled),
                        isSold = form.isSold,
                        soldDate = form.soldDate?.format(
                            DateTimeFormatter.ofLocalizedDate(
                                FormatStyle.SHORT
                            )
                        ),
                        soldStatusText = if (form.formType == FormType.EDIT) {
                            if (form.isSold) NativeText.Resource(R.string.form_sold_text)
                            else NativeText.Resource(R.string.form_for_sale_text)
                        } else null,
                        entryDateText = if (form.formType == FormType.ADD) {
                            form.lastEditionDate?.let {
                                NativeText.Argument(
                                    R.string.form_entry_date_tv,
                                    it.format(
                                        DateTimeFormatter.ofLocalizedDate(
                                            FormatStyle.MEDIUM
                                        )
                                    )
                                )
                            }
                        } else null,
                        isAddressValid = if (isInternetEnabled) form.isAddressValid else false,
                        areEditItemsVisible = form.formType == FormType.EDIT,
                        isInternetEnabled = isInternetEnabled,
                    )
                }.collectLatest {
                    emit(it)
                }
            }

// Save form throttle
            launch {
                formMutableStateFlow.transform {
                    emit(it)
                    delay(5.seconds)
                }.collect {
                    updatePropertyFormUseCase.invoke(it)
                }
            }

// Save draft when title is set (case when FormType.ADD and when title is null)
            launch {
                getFormTypeAndTitleAsFlowUseCase.invoke().collect { formTypeAndTitle ->
                    if (formTypeAndTitle.formType == FormType.ADD && formMutableStateFlow.value.draftTitle == null) {
                        formMutableStateFlow.update {
                            it.copy(draftTitle = formTypeAndTitle.title)
                        }
                        updatePropertyFormUseCase.invoke(formMutableStateFlow.value)
                    }
                }
            }

// Save draft when navigating away
            launch {
                getSavePropertyDraftEventUseCase.invoke().collect {
                    updatePropertyFormUseCase.invoke(formMutableStateFlow.value)
                }
            }

// Clear draft when navigating away
            launch {
                getClearPropertyFormNavigationEventAsFlowUseCase.invoke().collect {
                    resetPropertyFormUseCase.invoke(formMutableStateFlow.value.id)
                }
            }
        }
    }

    private fun mapPicturePreviews(
        picturePreviews: List<PicturePreviewEntity>,
        form: FormDraftParams
    ): List<PicturePreviewStateItem> =
        picturePreviews.map { picturePreview ->
            PicturePreviewStateItem.AddPropertyPicturePreview(
                id = picturePreview.id,
                uri = picturePreview.uri,
                isFeatured = picturePreview.isFeatured,
                description = picturePreview.description,
                onDeleteEvent = EquatableCallback {
                    if (form.featuredPictureId == picturePreview.id) {
                        val newFeaturedId = form.pictureIds.find { it != picturePreview.id }
                        formMutableStateFlow.update {
                            it.copy(
                                pictureIds = it.pictureIds.filter { id -> id != picturePreview.id },
                                featuredPictureId = newFeaturedId
                            )
                        }
                        viewModelScope.launch {
                            if (newFeaturedId != null) {
                                updatePicturePreviewUseCase.invoke(
                                    newFeaturedId,
                                    true,
                                    picturePreviews.find { it.id == newFeaturedId }?.description
                                )
                            }
                        }
                    } else {
                        formMutableStateFlow.update {
                            it.copy(
                                pictureIds = it.pictureIds.filter { id -> id != picturePreview.id }
                            )
                        }
                    }
                    viewModelScope.launch {
                        deletePicturePreviewUseCase.invoke(picturePreview.id, picturePreview.uri)
                    }
                },
                onFeaturedEvent = EquatableCallbackWithParam { isFeatured ->
                    if (picturePreview.isFeatured) return@EquatableCallbackWithParam

                    viewModelScope.launch {
                        updatePicturePreviewUseCase.invoke(
                            picturePreview.id,
                            isFeatured,
                            picturePreview.description
                        )
                    }
                    formMutableStateFlow.update {
                        it.copy(featuredPictureId = picturePreview.id)
                    }
                },
                onDescriptionChanged = EquatableCallbackWithParam { description ->
                    viewModelScope.launch {
                        updatePicturePreviewUseCase.invoke(
                            picturePreview.id,
                            picturePreview.isFeatured,
                            description
                        )
                    }
                },
            )
        }

    private fun mapPredictionsToViewState(
        currentPredictionAddresses: PredictionWrapper?,
        isInternetEnabled: Boolean
    ): List<PredictionViewState> =
        if (isInternetEnabled) {
            when (currentPredictionAddresses) {
                is PredictionWrapper.Success -> {
                    currentPredictionAddresses.predictions.map { prediction ->
                        PredictionViewState.Prediction(
                            address = prediction,
                            onClickEvent = EquatableCallbackWithParam { selectedAddress ->
                                formMutableStateFlow.update {
                                    it.copy(
                                        address = selectedAddress,
                                        isAddressValid = true
                                    )
                                }
                                viewModelScope.launch {
                                    updateOnAddressClickedUseCase.invoke(
                                        true,
                                        formMutableStateFlow.value.id
                                    )
                                }
                            }
                        )
                    }
                }

                is PredictionWrapper.NoResult -> listOf(PredictionViewState.EmptyState)
                is PredictionWrapper.Error -> emptyList<PredictionViewState>().also { println("Error: ${currentPredictionAddresses.error}") }
                is PredictionWrapper.Failure -> emptyList<PredictionViewState>().also {
                    println("Failure: ${currentPredictionAddresses.failure}")
                }

                else -> emptyList()
            }
        } else {
            formMutableStateFlow.update {
                it.copy(
                    isAddressValid = false // we can't say for sure if the address is valid without internet
                )
            }
            emptyList()
        }

    private fun mapAmenityTypesToViewStates(amenityTypes: List<AmenityType>): List<AmenityViewState> =
        amenityTypes.map { amenityType ->
            AmenityViewState.AmenityCheckbox(
                id = amenityType.id,
                name = amenityType.name,
                iconDrawable = amenityType.iconDrawable,
                stringRes = amenityType.stringRes,
                isChecked = formMutableStateFlow.value.selectedAmenities.contains(amenityType),
                onCheckBoxClicked = EquatableCallbackWithParam { isChecked ->
                    if (formMutableStateFlow.value.selectedAmenities.contains(amenityType) && !isChecked) {
                        formMutableStateFlow.update {
                            it.copy(selectedAmenities = it.selectedAmenities - amenityType)
                        }
                    } else if (!formMutableStateFlow.value.selectedAmenities.contains(amenityType) && isChecked) {
                        formMutableStateFlow.update {
                            it.copy(selectedAmenities = it.selectedAmenities + amenityType)
                        }
                    }
                },
            )
        }

    fun onPropertyTypeSelected(databaseName: String) {
        formMutableStateFlow.update {
            it.copy(
                typeDatabaseName = databaseName,
            )
        }
    }

    fun onAgentSelected(agent: String) {
        formMutableStateFlow.update {
            it.copy(agent = agent)
        }
    }

    fun onAddressChanged(input: String?) {
        viewModelScope.launch {
            isInternetEnabledFlowUseCase.invoke().firstOrNull()?.let { isInternetEnabled ->
                if (!isInternetEnabled) {
                    formMutableStateFlow.update {
                        it.copy(
                            address = input,
                        )
                    }
                    return@launch
                }
            }
        }

        if (formMutableStateFlow.value.isAddressValid && formMutableStateFlow.value.address != input) {
            viewModelScope.launch { updateOnAddressClickedUseCase.invoke(false, formMutableStateFlow.value.id) }
            formMutableStateFlow.update {
                it.copy(isAddressValid = false)
            }
        }
        viewModelScope.launch { setSelectedAddressStateUseCase.invoke(input) }
        formMutableStateFlow.update {
            it.copy(address = input?.ifBlank { null })
        }
    }

    fun onAddressEditTextFocused(hasFocus: Boolean) {
        setHasAddressFocusUseCase.invoke(hasFocus)
    }

    fun onPriceChanged(price: String?) {
        formMutableStateFlow.update { form ->
            form.copy(price = if (price.isNullOrBlank()) BigDecimal.ZERO else BigDecimal(price))
        }
    }

    fun onSurfaceChanged(surface: String?) {
        formMutableStateFlow.update { form ->
            form.copy(surface = if (surface.isNullOrBlank()) BigDecimal.ZERO else BigDecimal(surface))
        }
    }

    fun onPictureSelected(stringUri: String) {
        viewModelScope.launch {
            val addedPicturePreviewId = savePictureToLocalAppFilesAndToLocalDatabaseUseCase.invoke(
                stringUri = stringUri,
                isFormPictureIdEmpty = formMutableStateFlow.value.pictureIds.isEmpty(),
                formMutableStateFlow.value.id
            )
            formMutableStateFlow.update {
                if (it.pictureIds.isEmpty()) {
                    it.copy(
                        pictureIds = listOf(addedPicturePreviewId),
                        featuredPictureId = addedPicturePreviewId
                    )
                } else {
                    it.copy(pictureIds = it.pictureIds + addedPicturePreviewId)
                }
            }
        }
    }

    private var updateDescription: Job? = null

    fun onDescriptionChanged(description: String) {
        updateDescription = viewModelScope.launch {
            updateDescription?.cancel()
            delay(1.seconds)
            formMutableStateFlow.update {
                it.copy(description = description.ifBlank { null })
            }
        }
    }

    fun onRoomsNumberChanged(value: Int) {
        formMutableStateFlow.update {
            it.copy(nbRooms = value)
        }
    }

    fun onBedroomsNumberChanged(value: Int) {
        formMutableStateFlow.update {
            it.copy(nbBedrooms = value)
        }
    }

    fun onBathroomsNumberChanged(value: Int) {
        formMutableStateFlow.update {
            it.copy(nbBathrooms = value)
        }
    }

    fun onSoldStatusChanged(checked: Boolean) {
        formMutableStateFlow.update {
            it.copy(isSold = checked)
        }
    }

    fun onAddPropertyClicked() {
        onSubmitButtonClickedMutableSharedFlow.tryEmit(Unit)
    }
}

