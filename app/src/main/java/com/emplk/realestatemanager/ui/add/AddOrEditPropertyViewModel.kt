package com.emplk.realestatemanager.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.R
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
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.draft.GetClearPropertyFormNavigationEventUseCase
import com.emplk.realestatemanager.domain.navigation.draft.GetDraftNavigationUseCase
import com.emplk.realestatemanager.domain.property.AddOrEditPropertyUseCase
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property.amenity.type.GetAmenityTypeUseCase
import com.emplk.realestatemanager.domain.property_draft.ClearPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.FormDraftParams
import com.emplk.realestatemanager.domain.property_draft.GetFormTitleAsFlowUseCase
import com.emplk.realestatemanager.domain.property_draft.InitPropertyFormUseCase
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
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
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
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase,
    private val getDraftNavigationUseCase: GetDraftNavigationUseCase,
    private val getClearPropertyFormNavigationEventUseCase: GetClearPropertyFormNavigationEventUseCase,
    private val clearPropertyFormUseCase: ClearPropertyFormUseCase,
    private val updatePicturePreviewUseCase: UpdatePicturePreviewUseCase,
    private val addAllPicturePreviewsIdsUseCase: AddAllPicturePreviewsIdsUseCase,
    private val savePictureToLocalAppFilesAndToLocalDatabaseUseCase: SavePictureToLocalAppFilesAndToLocalDatabaseUseCase,
    private val updatePropertyFormUseCase: UpdatePropertyFormUseCase,
    private val initPropertyFormUseCase: InitPropertyFormUseCase,
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase,
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
    private val getPicturePreviewsAsFlowUseCase: GetPicturePreviewsAsFlowUseCase,
    private val deletePicturePreviewUseCase: DeletePicturePreviewUseCase,
    private val getAgentsMapUseCase: GetAgentsMapUseCase,
    private val convertPriceDependingOnLocaleUseCase: ConvertPriceDependingOnLocaleUseCase,
    private val convertToSquareFeetDependingOnLocaleUseCase: ConvertToSquareFeetDependingOnLocaleUseCase,
    private val getFormTitleUseCase: GetFormTitleAsFlowUseCase,
    private val setFormTitleUseCase: SetFormTitleUseCase,
    private val setSelectedAddressStateUseCase: SetSelectedAddressStateUseCase,
    private val updateOnAddressClickedUseCase: UpdateOnAddressClickedUseCase,
    private val setHasAddressFocusUseCase: SetHasAddressFocusUseCase,
    private val setPropertyFormProgressUseCase: SetPropertyFormProgressUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val getAmenityTypeUseCase: GetAmenityTypeUseCase,
    private val getCurrentPredictionAddressesFlowWithDebounceUseCase: GetCurrentPredictionAddressesFlowWithDebounceUseCase,
    private val getPropertyTypeUseCase: GetPropertyTypeUseCase,
    private val isInternetEnabledFlowUseCase: IsInternetEnabledFlowUseCase,
) : ViewModel() {

    private val formMutableStateFlow = MutableStateFlow(FormDraftParams())

    private val isFormValidMutableStateFlow = MutableStateFlow(false)
    private val isAddingPropertyInDatabaseMutableStateFlow = MutableStateFlow(false)
    private val onCreateButtonClickedMutableSharedFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val viewEventLiveData: LiveData<Event<FormEvent>> = liveData {
        onCreateButtonClickedMutableSharedFlow.collect {
            combine(
                formMutableStateFlow,
                isInternetEnabledFlowUseCase.invoke()
            ) { form, isInternetEnabled ->
                if (isInternetEnabled) {
                    isAddingPropertyInDatabaseMutableStateFlow.tryEmit(true)
                    val resultEvent = addOrEditPropertyUseCase.invoke(form)
                    emit(Event(resultEvent))
                    isAddingPropertyInDatabaseMutableStateFlow.tryEmit(false)
                } else {
                    updatePropertyFormUseCase.invoke(form)
                    setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                    emit(Event(FormEvent.Toast(NativeText.Resource(R.string.no_internet_connection_draft_saved))))
                }
            }.collect()
        }
    }

    val viewStateLiveData: LiveData<PropertyFormViewState> = liveData {
        coroutineScope {
            if (latestValue == null) {
                emit(PropertyFormViewState.LoadingState)
            }

            val propertyId = getCurrentPropertyIdFlowUseCase.invoke().firstOrNull()
            val formWithType = initPropertyFormUseCase.invoke(propertyId)

            formMutableStateFlow.update { form ->
                form.copy(
                    id = formWithType.formDraftEntity.id,
                    formType = formWithType.formType,
                    propertyType = formWithType.formDraftEntity.type,
                    draftTitle = formWithType.formDraftEntity.title,
                    address = formWithType.formDraftEntity.address,
                    isAddressValid = formWithType.formDraftEntity.isAddressValid,
                    price = convertPriceDependingOnLocaleUseCase.invoke(formWithType.formDraftEntity.price),  // TODO: voir si pas trop appelé
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
                    entryDateEpoch = formWithType.formDraftEntity.entryDateEpoch,
                    isSold = formWithType.formDraftEntity.isSold,
                    soldDate = formWithType.formDraftEntity.saleDate,
                )
            }
            if (formWithType.formType == FormType.ADD) setFormTitleUseCase.invoke(formWithType.formType, null)

            addAllPicturePreviewsIdsUseCase.invoke(formMutableStateFlow.value.pictureIds)
            setFormTitleUseCase.invoke(formWithType.formType, formWithType.formDraftEntity.title)

            launch {
                combine(
                    formMutableStateFlow,
                    getPicturePreviewsAsFlowUseCase.invoke(formMutableStateFlow.value.id),
                    getCurrentPredictionAddressesFlowWithDebounceUseCase.invoke(),
                    isAddingPropertyInDatabaseMutableStateFlow,
                ) { form, picturePreviews, predictionWrapper, isAddingInDatabase ->
                    val currencyType = getCurrencyTypeUseCase.invoke()
                    val amenityTypes = getAmenityTypeUseCase.invoke()
                    val propertyTypes = getPropertyTypeUseCase.invoke()
                    val agents = getAgentsMapUseCase.invoke()

                    val isFormInProgress = !form.propertyType.isNullOrBlank() ||
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

                    setPropertyFormProgressUseCase.invoke(isFormInProgress)

                    isFormValidMutableStateFlow.tryEmit(
                        form.propertyType != null &&
                                form.address != null &&
                                form.isAddressValid &&
                                form.price > BigDecimal.ZERO &&
                                form.surface > BigDecimal.ZERO &&
                                form.description != null &&
                                form.nbRooms > 0 &&
                                form.nbBathrooms > 0 &&
                                form.nbBedrooms > 0 &&
                                form.agent != null &&
                                form.selectedAmenities.isNotEmpty() &&
                                form.pictureIds.isNotEmpty() &&
                                form.featuredPictureId != null
                    )

                    PropertyFormViewState.PropertyForm(
                        propertyType = form.propertyType,
                        address = form.address,
                        price = if (form.price == BigDecimal.ZERO) "" else form.price.toString(),
                        surface = if (form.surface == BigDecimal.ZERO) "" else form.surface.toString(),
                        description = form.description,
                        nbRooms = form.nbRooms,
                        nbBathrooms = form.nbBathrooms,
                        nbBedrooms = form.nbBedrooms,
                        pictures = mapPicturePreviews(picturePreviews, form),
                        selectedAgent = form.agent,
                        priceCurrencyHint = when (currencyType) {
                            CurrencyType.DOLLAR -> NativeText.Resource(R.string.price_in_dollar)

                            CurrencyType.EURO -> NativeText.Resource(R.string.price_in_euro)
                        },
                        currencyDrawableRes = when (currencyType) {
                            CurrencyType.DOLLAR -> R.drawable.baseline_dollar_24
                            CurrencyType.EURO -> R.drawable.baseline_euro_24
                        },
                        surfaceUnit = NativeText.Argument(
                            R.string.surface_area_unit_in_n,
                            getSurfaceUnitUseCase.invoke().symbol,
                        ),
                        propertyCreationDate = if (form.formType == FormType.EDIT) {
                            form.entryDate?.let {
                                NativeText.Argument(
                                    R.string.form_creation_date_tv,
                                    it.format(
                                        DateTimeFormatter.ofLocalizedDateTime(
                                            FormatStyle.SHORT
                                        )
                                    )
                                )
                            }
                        } else null,
                        isSubmitButtonEnabled = isFormValidMutableStateFlow.value,
                        submitButtonText = if (form.formType == FormType.ADD) NativeText.Resource(R.string.form_create_button)
                        else NativeText.Resource(R.string.form_edit_button),
                        isProgressBarVisible = isAddingInDatabase,
                        amenities = mapAmenityTypesToViewStates(amenityTypes),
                        propertyTypes = propertyTypes.map { propertyType ->
                            PropertyTypeViewStateItem(
                                id = propertyType.key,
                                name = propertyType.value,
                            )
                        },
                        agents = agents.map { agent ->
                            AddPropertyAgentViewStateItem(
                                id = agent.key,
                                name = agent.value
                            )
                        },
                        addressPredictions = mapPredictionsToViewState(predictionWrapper),
                        isSold = form.isSold,
                        soldDate = form.soldDate?.format(
                            DateTimeFormatter.ofLocalizedDate(
                                FormatStyle.SHORT
                            )
                        ),
                        isAddressValid = form.isAddressValid,
                        areEditItemsVisible = form.formType == FormType.EDIT,
                    )
                }.collectLatest {
                    emit(it)
                }
            }

// Throttle
            launch {
                formMutableStateFlow.transform {
                    emit(it)
                    delay(10.seconds)
                }.collect {
                    updatePropertyFormUseCase.invoke(it)
                }
            }

// Save draft when title is set (case when FormType.ADD and when title is null)
            launch {
                getFormTitleUseCase.invoke().collect { formTypeAndTitle ->
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
                getDraftNavigationUseCase.invoke().collect {
                    updatePropertyFormUseCase.invoke(formMutableStateFlow.value)
                }
            }

// Clear draft when navigating away
            launch {
                getClearPropertyFormNavigationEventUseCase.invoke().collect {
                    clearPropertyFormUseCase.invoke(formMutableStateFlow.value.id)
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

    private fun mapPredictionsToViewState(currentPredictionAddresses: PredictionWrapper?): List<PredictionViewState> {
        return when (currentPredictionAddresses) {
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
                            } // TODO: why tf if I put it above formMutableStateFlow.update it doesn't work
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

    fun onPropertyTypeSelected(propertyType: String) {
        formMutableStateFlow.update {
            it.copy(propertyType = propertyType)
        }
    }

    fun onAgentSelected(agent: String) {
        formMutableStateFlow.update {
            it.copy(agent = agent)
        }
    }

    fun onAddressChanged(input: String?) {
        if (formMutableStateFlow.value.isAddressValid && formMutableStateFlow.value.address != input) {
            viewModelScope.launch { updateOnAddressClickedUseCase.invoke(false, formMutableStateFlow.value.id) }
            formMutableStateFlow.update {
                it.copy(isAddressValid = false)
            }
        }
        setSelectedAddressStateUseCase.invoke(input)
        formMutableStateFlow.update {
            it.copy(address = input)
        }
    }

    fun onAddressEditTextFocused(hasFocus: Boolean) {
        setHasAddressFocusUseCase.invoke(hasFocus)
    }

    fun onPriceChanged(price: BigDecimal) {
        if (price > BigDecimal.ZERO) {
            formMutableStateFlow.update {
                it.copy(price = price)
            }
        }
    }

    fun onSurfaceChanged(surface: String?) {
        if (surface.isNullOrBlank()) return
        formMutableStateFlow.update {
            it.copy(surface = surface.toBigDecimal())
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

    fun onDescriptionChanged(description: String) {
        formMutableStateFlow.update {
            it.copy(description = description)
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
        onCreateButtonClickedMutableSharedFlow.tryEmit(Unit)
    }
}

