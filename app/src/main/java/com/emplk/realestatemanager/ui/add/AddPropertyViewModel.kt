package com.emplk.realestatemanager.ui.add

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.agent.GetAgentsFlowUseCase
import com.emplk.realestatemanager.domain.autocomplete.GetAddressPredictionsUseCase
import com.emplk.realestatemanager.domain.autocomplete.PredictionWrapper
import com.emplk.realestatemanager.domain.connectivity.IsInternetEnabledFlowUseCase
import com.emplk.realestatemanager.domain.locale_formatting.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.navigation.draft.GetDraftNavigationUseCase
import com.emplk.realestatemanager.domain.property.AddPropertyUseCase
import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property.amenity.type.GetAmenityTypeUseCase
import com.emplk.realestatemanager.domain.property_draft.AddPropertyFormEntity
import com.emplk.realestatemanager.domain.property_draft.InitTemporaryPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.PropertyFormDatabaseState
import com.emplk.realestatemanager.domain.property_draft.SetPropertyFormProgressUseCase
import com.emplk.realestatemanager.domain.property_draft.UpdatePropertyFormUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.DeletePicturePreviewByIdUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.GetPicturePreviewsAsFlowUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.SavePictureToLocalAppFilesAndToLocalDatabaseUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.UpdatePicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_draft.picture_preview.id.DeletePicturePreviewIdUseCase
import com.emplk.realestatemanager.domain.property_type.GetPropertyTypeFlowUseCase
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionViewState
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentViewStateItem
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewState
import com.emplk.realestatemanager.ui.add.picture_preview.PicturePreviewStateItem
import com.emplk.realestatemanager.ui.add.type.AddPropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val addPropertyUseCase: AddPropertyUseCase,
    private val getDraftNavigationUseCase: GetDraftNavigationUseCase,
    private val deletePicturePreviewIdUseCase: DeletePicturePreviewIdUseCase, // à refacto
    private val deletePicturePreviewByIdUseCase: DeletePicturePreviewByIdUseCase, // à refacto
    private val updatePicturePreviewUseCase: UpdatePicturePreviewUseCase,
    private val savePictureToLocalAppFilesAndToLocalDatabaseUseCase: SavePictureToLocalAppFilesAndToLocalDatabaseUseCase,
    private val updatePropertyFormUseCase: UpdatePropertyFormUseCase, // à refacto si chui une ouf
    private val getAddressPredictionsUseCase: GetAddressPredictionsUseCase, // à refacto
    private val initTemporaryPropertyFormUseCase: InitTemporaryPropertyFormUseCase,
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase,
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
    private val getPicturePreviewsAsFlowUseCase: GetPicturePreviewsAsFlowUseCase,
    private val getAgentsFlowUseCase: GetAgentsFlowUseCase,
    private val setPropertyFormProgressUseCase: SetPropertyFormProgressUseCase,
    private val getAmenityTypeUseCase: GetAmenityTypeUseCase,
    private val getPropertyTypeFlowUseCase: GetPropertyTypeFlowUseCase,
    private val isInternetEnabledFlowUseCase: IsInternetEnabledFlowUseCase,  // à garder I suppose
) : ViewModel() {

    private val formMutableStateFlow = MutableStateFlow(AddPropertyFormEntity())
    private val currentAddressInputMutableStateFlow: MutableStateFlow<String?> = MutableStateFlow(null)
    private val perfectMatchPredictionMutableStateFlow = MutableStateFlow<Boolean?>(null)  // TODO not sure at all!..
    private val hasAddressEditTextFocus = MutableStateFlow(false)
    private val isEveryFieldFilledMutableStateFlow = MutableStateFlow(false)
    private val isAddingPropertyInDatabaseMutableStateFlow = MutableStateFlow(false)
    private val onCreateButtonClickedMutableSharedFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val currentPredictionAddressesFlow: Flow<PredictionWrapper?> =
        currentAddressInputMutableStateFlow.transformLatest { input ->
            if (input.isNullOrBlank() || input.length < 3 || perfectMatchPredictionMutableStateFlow.value == true || hasAddressEditTextFocus.value.not()) {
                emit(null)
            } else {
                delay(400.milliseconds)
                emit(getAddressPredictionsUseCase.invoke(input))
            }
        }

    val viewEventLiveData: LiveData<Event<AddPropertyEvent>> = liveData {
        onCreateButtonClickedMutableSharedFlow.collect {
            combine(
                formMutableStateFlow,
                isInternetEnabledFlowUseCase.invoke()
            ) { form, isInternetEnabled ->
                if (true) { // TODO: change that of course
                    isAddingPropertyInDatabaseMutableStateFlow.tryEmit(true)
                    val resultEvent = addPropertyUseCase.invoke(form)
                    emit(Event(resultEvent))
                    isAddingPropertyInDatabaseMutableStateFlow.tryEmit(false)
                } else {
                    updatePropertyFormUseCase.invoke(form)
                    emit(Event(AddPropertyEvent.Toast(NativeText.Resource(R.string.no_internet_connection_draft_saved))))
                }
            }.collect()
        }
    }

    val viewStateLiveData: LiveData<AddPropertyViewState> = liveData {
        coroutineScope {
            when (val initTemporaryPropertyFormUseCase = initTemporaryPropertyFormUseCase.invoke()) {
                is PropertyFormDatabaseState.Empty -> Log.d(
                    "AddPropertyViewModel",
                    "initTemporaryPropertyFormUseCase with new id: ${initTemporaryPropertyFormUseCase.newPropertyFormId}"
                )

                is PropertyFormDatabaseState.DraftAlreadyExists -> {
                    formMutableStateFlow.update { addPropertyForm ->
                        addPropertyForm.copy(
                            propertyType = initTemporaryPropertyFormUseCase.propertyDraftEntity.type,
                            address = initTemporaryPropertyFormUseCase.propertyDraftEntity.address,
                            price = initTemporaryPropertyFormUseCase.propertyDraftEntity.price,
                            surface = initTemporaryPropertyFormUseCase.propertyDraftEntity.surface,
                            description = initTemporaryPropertyFormUseCase.propertyDraftEntity.description,
                            nbRooms = initTemporaryPropertyFormUseCase.propertyDraftEntity.rooms ?: 0,
                            nbBathrooms = initTemporaryPropertyFormUseCase.propertyDraftEntity.bathrooms ?: 0,
                            nbBedrooms = initTemporaryPropertyFormUseCase.propertyDraftEntity.bedrooms ?: 0,
                            agent = initTemporaryPropertyFormUseCase.propertyDraftEntity.agentName,
                            amenities = initTemporaryPropertyFormUseCase.propertyDraftEntity.amenities,
                            pictureIds = initTemporaryPropertyFormUseCase.propertyDraftEntity.pictures.map { it.id },
                            featuredPictureId = initTemporaryPropertyFormUseCase.propertyDraftEntity.pictures.find { it.isFeatured }?.id,
                        )
                    }

                    Log.d(
                        "AddPropertyViewModel",
                        "initTemporaryPropertyFormUseCase with existing propertyForm: ${initTemporaryPropertyFormUseCase.propertyDraftEntity}"
                    )
                }
            }

            launch {
                combine(
                    formMutableStateFlow,
                    getAgentsFlowUseCase.invoke(),
                    getPicturePreviewsAsFlowUseCase.invoke(),
                    currentPredictionAddressesFlow,
                    isAddingPropertyInDatabaseMutableStateFlow,
                ) { form, agents, picturePreviews, currentPredictionAddresses, isAddingPropertyInDatabase ->
                    val currencyType = getCurrencyTypeUseCase.invoke()
                    val amenityTypes = getAmenityTypeUseCase.invoke()
                    val propertyTypes = getPropertyTypeFlowUseCase.invoke()

                    val isFormInProgress = !form.propertyType.isNullOrBlank() ||
                            !form.address.isNullOrBlank() ||
                            (form.price > BigDecimal.ZERO) ||
                            !form.surface.isNullOrBlank() ||
                            !form.description.isNullOrBlank() ||
                            form.nbRooms > 0 ||
                            form.nbBathrooms > 0 ||
                            form.nbBedrooms > 0 ||
                            !form.agent.isNullOrBlank() ||
                            form.amenities.isNotEmpty() ||
                            form.pictureIds.isNotEmpty() ||
                            form.featuredPictureId != null

                    setPropertyFormProgressUseCase.invoke(isFormInProgress)
                    isEveryFieldFilledMutableStateFlow.tryEmit(
                        form.propertyType != null &&
                                form.address != null &&
                                (form.price > BigDecimal.ZERO) &&
                                form.surface != null &&
                                form.description != null &&
                                form.nbRooms > 0 &&
                                form.nbBathrooms > 0 &&
                                form.nbBedrooms > 0 &&
                                form.agent != null &&
                                form.amenities.isNotEmpty() &&
                                form.pictureIds.isNotEmpty() &&
                                form.featuredPictureId != null
                    )

                    emit(
                        AddPropertyViewState(
                            propertyType = form.propertyType,
                            address = form.address,
                            price = if (form.price == BigDecimal.ZERO) "" else form.price.toString(),  // TODO: amarchpo
                            surface = form.surface,
                            description = form.description,
                            nbRooms = form.nbRooms,
                            nbBathrooms = form.nbBathrooms,
                            nbBedrooms = form.nbBedrooms,
                            pictures = picturePreviews
                                .map { picturePreview ->
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
                                            }

                                            formMutableStateFlow.update {
                                                it.copy(
                                                    pictureIds = it.pictureIds.filter { id -> id != picturePreview.id }
                                                )
                                            }

                                            viewModelScope.launch {
                                                deletePicturePreviewByIdUseCase.invoke(picturePreview.id)
                                            }
                                            deletePicturePreviewIdUseCase.invoke(picturePreview.id)
                                        },
                                        onFeaturedEvent = EquatableCallbackWithParam { isFeatured ->
                                            if (picturePreview.isFeatured) return@EquatableCallbackWithParam
                                            formMutableStateFlow.update {
                                                it.copy(featuredPictureId = picturePreview.id)
                                            }
                                            viewModelScope.launch {
                                                updatePicturePreviewUseCase.invoke(
                                                    picturePreview.id,
                                                    isFeatured,
                                                    picturePreview.description
                                                )
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
                                },
                            selectedAgent = form.agent,
                            priceCurrency = when (currencyType) {
                                CurrencyType.DOLLAR -> NativeText.Argument(
                                    R.string.price_currency_in_n,
                                    currencyType.symbol
                                )

                                CurrencyType.EURO -> NativeText.Argument(
                                    R.string.price_currency_in_n,
                                    currencyType.symbol
                                )
                            },
                            surfaceUnit = NativeText.Argument(
                                R.string.surface_unit_in_n,
                                getSurfaceUnitUseCase.invoke().symbol,
                            ),
                            isAddButtonEnabled = isEveryFieldFilledMutableStateFlow.value,
                            isProgressBarVisible = isAddingPropertyInDatabase,
                            amenities = mapAmenityTypesToViewStates(amenityTypes),
                            selectedAmenities = form.amenities,
                            propertyTypes = propertyTypes.map { propertyType ->
                                AddPropertyTypeViewStateItem(
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
                            addressPredictions = mapPredictionsToViewState(currentPredictionAddresses),
                        )
                    )
                }.collect()
            }

            // Throttle
            launch {
                formMutableStateFlow.transform {
                    emit(it)
                    delay(10.seconds)
                }.collect {
                    updatePropertyFormUseCase.invoke(formMutableStateFlow.value)
                }
            }

            // Save draft when navigating away
            launch {
                getDraftNavigationUseCase.invoke().collectLatest {
                    formMutableStateFlow.map { form ->
                        updatePropertyFormUseCase.invoke(form)
                    }.collect()
                }
            }
        }
    }

    private fun mapPredictionsToViewState(currentPredictionAddresses: PredictionWrapper?): List<PredictionViewState> {
        return when (currentPredictionAddresses) {
            is PredictionWrapper.Success -> {
                currentPredictionAddresses.predictions.map { prediction ->
                    PredictionViewState.Prediction(
                        address = prediction,
                        onClickEvent = EquatableCallbackWithParam { selectedAddress ->
                            perfectMatchPredictionMutableStateFlow.tryEmit(true)
                            currentAddressInputMutableStateFlow.tryEmit(null)
                            formMutableStateFlow.update {
                                it.copy(
                                    address = selectedAddress,
                                    addressPredictions = emptyList(),
                                )
                            }
                        }
                    )
                }
            }

            is PredictionWrapper.NoResult -> listOf(PredictionViewState.EmptyState)
            is PredictionWrapper.Error -> emptyList()
            is PredictionWrapper.Failure -> emptyList()

            null -> emptyList()
        }
    }


    private fun mapAmenityTypesToViewStates(amenityTypes: List<AmenityType>): List<AmenityViewState> =
        amenityTypes.map { amenityType ->
            AmenityViewState.AmenityCheckbox(
                id = amenityType.id,
                name = amenityType.name,
                isChecked = formMutableStateFlow.value.amenities.any { amenity -> amenity.type.name == amenityType.name },
                iconDrawable = amenityType.iconDrawable,
                stringRes = amenityType.stringRes,
                onCheckBoxClicked = EquatableCallbackWithParam { isChecked ->
                    if (isChecked) {
                        formMutableStateFlow.update {
                            it.copy(
                                amenities = it.amenities + AmenityEntity(
                                    id = amenityType.id,
                                    type = amenityType,
                                )
                            )
                        }
                    } else {
                        formMutableStateFlow.update {
                            it.copy(
                                amenities = it.amenities.filter { amenity -> amenity.id != amenityType.id }
                            )
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
        if (input.isNullOrBlank()) {
            perfectMatchPredictionMutableStateFlow.tryEmit(null)
            currentAddressInputMutableStateFlow.tryEmit(null)
            formMutableStateFlow.update {
                it.copy(
                    address = input,
                    addressPredictions = emptyList(),
                )
            }
        } else if (perfectMatchPredictionMutableStateFlow.value == true) {
            currentAddressInputMutableStateFlow.tryEmit(input)
            formMutableStateFlow.update {
                it.copy(
                    address = input,
                    addressPredictions = emptyList(),
                )
            }
        } else {
            perfectMatchPredictionMutableStateFlow.tryEmit(false)
            currentAddressInputMutableStateFlow.tryEmit(input)
            formMutableStateFlow.update {
                it.copy(
                    address = input,
                )
            }
        }
    }

    fun onPriceChanged(price: BigDecimal) {
        if (price > BigDecimal.ZERO) {
            formMutableStateFlow.update {
                it.copy(price = price)
            }
        }
    }

    fun onSurfaceChanged(surface: String?) {
        formMutableStateFlow.update {
            it.copy(surface = surface)
        }
    }

    fun onPictureSelected(stringUri: String) {
        viewModelScope.launch {
            val addedPicturePreviewId = savePictureToLocalAppFilesAndToLocalDatabaseUseCase.invoke(
                stringUri = stringUri,
                isFormPictureIdEmpty = formMutableStateFlow.value.pictureIds.isEmpty()
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

    fun onAddPropertyClicked() {
        onCreateButtonClickedMutableSharedFlow.tryEmit(Unit)
    }

    fun onAddressEditTextFocused(hasFocus: Boolean) {
        hasAddressEditTextFocus.tryEmit(hasFocus)
    }
}

