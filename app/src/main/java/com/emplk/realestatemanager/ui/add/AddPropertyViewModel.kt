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
import com.emplk.realestatemanager.domain.content_resolver.SaveFileToLocalAppFilesUseCase
import com.emplk.realestatemanager.domain.geocoding.GeocodingWrapper
import com.emplk.realestatemanager.domain.geocoding.GetAddressLatLongUseCase
import com.emplk.realestatemanager.domain.locale_formatting.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.map_picture.GenerateMapBaseUrlWithParamsUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.AddPropertyUseCase
import com.emplk.realestatemanager.domain.property.PropertyEntity
import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property.amenity.type.GetAmenityTypeUseCase
import com.emplk.realestatemanager.domain.property.location.LocationEntity
import com.emplk.realestatemanager.domain.property.pictures.PictureEntity
import com.emplk.realestatemanager.domain.property_form.DeleteTemporaryPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_form.GetSavedPropertyFormEventUseCase
import com.emplk.realestatemanager.domain.property_form.InitTemporaryPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_form.PropertyFormDatabaseState
import com.emplk.realestatemanager.domain.property_form.PropertyFormEntity
import com.emplk.realestatemanager.domain.property_form.SetPropertyFormProgressUseCase
import com.emplk.realestatemanager.domain.property_form.UpdatePropertyFormUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.AddPicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.DeletePicturePreviewByIdUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.GetPicturePreviewsAsFlowUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.GetPicturePreviewsUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.UpdatePicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.id.AddPicturePreviewIdUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.id.DeleteAllPicturePreviewIdsUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.id.DeletePicturePreviewIdUseCase
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
import java.time.Clock
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val addPropertyUseCase: AddPropertyUseCase,
    private val getSavedPropertyFormEventUseCase: GetSavedPropertyFormEventUseCase,
    private val deleteTemporaryPropertyFormUseCase: DeleteTemporaryPropertyFormUseCase,
    private val addPicturePreviewUseCase: AddPicturePreviewUseCase,
    private val addPicturePreviewIdUseCase: AddPicturePreviewIdUseCase,
    private val saveFileToLocalAppFilesUseCase: SaveFileToLocalAppFilesUseCase,
    private val getPicturePreviewsUseCase: GetPicturePreviewsUseCase,
    private val deletePicturePreviewIdUseCase: DeletePicturePreviewIdUseCase,
    private val deletePicturePreviewByIdUseCase: DeletePicturePreviewByIdUseCase,
    private val deleteAllPicturePreviewIdsUseCase: DeleteAllPicturePreviewIdsUseCase,
    private val generateMapBaseUrlWithParamsUseCase: GenerateMapBaseUrlWithParamsUseCase,
    private val updatePicturePreviewUseCase: UpdatePicturePreviewUseCase,
    private val updatePropertyFormUseCase: UpdatePropertyFormUseCase,
    private val getAddressPredictionsUseCase: GetAddressPredictionsUseCase,
    private val getAddressLatLongUseCase: GetAddressLatLongUseCase,
    private val initTemporaryPropertyFormUseCase: InitTemporaryPropertyFormUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase,
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
    private val getPicturePreviewsAsFlowUseCase: GetPicturePreviewsAsFlowUseCase,
    private val getAgentsFlowUseCase: GetAgentsFlowUseCase,
    private val setPropertyFormProgressUseCase: SetPropertyFormProgressUseCase,
    private val getAmenityTypeUseCase: GetAmenityTypeUseCase,
    private val getPropertyTypeFlowUseCase: GetPropertyTypeFlowUseCase,
    private val isInternetEnabledFlowUseCase: IsInternetEnabledFlowUseCase,
    private val clock: Clock,
) : ViewModel() {

    private data class AddPropertyForm(
        val propertyType: String? = null,
        val address: String? = null,
        val addressPredictions: List<PredictionViewState> = emptyList(),
        val price: String? = null,
        val surface: String? = null,
        val description: String? = null,
        val nbRooms: Int = 0,
        val nbBathrooms: Int = 0,
        val nbBedrooms: Int = 0,
        val agent: String? = null,
        val amenities: List<AmenityEntity> = emptyList(),
        val pictureIds: List<Long> = emptyList(),
        val featuredPictureId: Long? = null,
    )

    private val formMutableStateFlow = MutableStateFlow(AddPropertyForm())
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
                    if (form.propertyType != null &&
                        form.address != null &&
                        form.price != null &&
                        form.surface != null &&
                        form.description != null &&
                        form.nbRooms > 0 &&
                        form.nbBathrooms > 0 &&
                        form.nbBedrooms > 0 &&
                        form.agent != null &&
                        form.amenities.isNotEmpty() &&
                        form.pictureIds.isNotEmpty()
                    ) {
                        val geocodingWrapper = getAddressLatLongUseCase.invoke(form.address)
                        when (geocodingWrapper) {
                            is GeocodingWrapper.Success -> geocodingWrapper.latLng
                            is GeocodingWrapper.Error -> Log.e(
                                "AddPropertyViewModel",
                                "Error: ${geocodingWrapper.error}"
                            )

                            is GeocodingWrapper.NoResult -> Log.e("AddPropertyViewModel", "No geocoding result")
                        }

                        val success = addPropertyUseCase.invoke(
                            PropertyEntity(
                                type = form.propertyType,
                                price = form.price.toBigDecimal(),
                                surface = form.surface.toInt(),
                                description = form.description,
                                rooms = form.nbRooms,
                                bathrooms = form.nbBathrooms,
                                location = LocationEntity(
                                    address = form.address,
                                    latLng = when (geocodingWrapper) {
                                        is GeocodingWrapper.Success -> geocodingWrapper.latLng
                                        is GeocodingWrapper.Error -> null
                                        is GeocodingWrapper.NoResult -> null
                                    },
                                    miniatureMapUrl = when (geocodingWrapper) {
                                        is GeocodingWrapper.Success ->
                                            generateMapBaseUrlWithParamsUseCase.invoke(geocodingWrapper.latLng)
                                        is GeocodingWrapper.Error -> ""
                                        is GeocodingWrapper.NoResult -> ""

                                    },
                                ),
                                bedrooms = form.nbBedrooms,
                                agentName = form.agent,
                                amenities = form.amenities,
                                pictures = getPicturePreviewsUseCase.invoke().map {
                                    PictureEntity(
                                        uri = it.uri,
                                        description = it.description ?: "",
                                        isFeatured = it.id == form.featuredPictureId,
                                    )
                                },
                                entryDate = LocalDateTime.now(clock),
                                isAvailableForSale = false,
                                isSold = false,
                                saleDate = null,
                            ),
                        )
                        isAddingPropertyInDatabaseMutableStateFlow.tryEmit(false)
                        if (success) {
                            deleteTemporaryPropertyFormUseCase.invoke()
                            deleteAllPicturePreviewIdsUseCase.invoke()
                            setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                            emit(
                                Event(
                                    AddPropertyEvent.Toast(
                                        NativeText.Resource(R.string.add_property_successfully_created_snackBar_message)
                                    )
                                )
                            )
                        } else {
                            emit(Event(AddPropertyEvent.Toast(NativeText.Resource(R.string.add_property_error_message))))
                        }
                    }
                }/* else {
                    updatePropertyFormUseCase.invoke(
                        PropertyFormEntity(
                            type = form.propertyType,
                            price = form.price,
                            surface = form.surface,
                            description = form.description,
                            rooms = form.nbRooms,
                            bathrooms = form.nbBathrooms,
                            address = form.address,
                            bedrooms = form.nbBedrooms,
                            agentName = form.agent,
                            amenities = form.amenities.map { amenity ->
                                AmenityEntity(
                                    id = amenity.id,
                                    type = amenity.type,
                                )
                            },
                        )
                    )
                    emit(Event(AddPropertyEvent.Toast(NativeText.Resource(R.string.no_internet_connection_draft_saved))))
                    setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                }*/
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
                            propertyType = initTemporaryPropertyFormUseCase.propertyFormEntity.type,
                            address = initTemporaryPropertyFormUseCase.propertyFormEntity.address,
                            price = initTemporaryPropertyFormUseCase.propertyFormEntity.price,
                            surface = initTemporaryPropertyFormUseCase.propertyFormEntity.surface,
                            description = initTemporaryPropertyFormUseCase.propertyFormEntity.description,
                            nbRooms = initTemporaryPropertyFormUseCase.propertyFormEntity.rooms ?: 0,
                            nbBathrooms = initTemporaryPropertyFormUseCase.propertyFormEntity.bathrooms ?: 0,
                            nbBedrooms = initTemporaryPropertyFormUseCase.propertyFormEntity.bedrooms ?: 0,
                            agent = initTemporaryPropertyFormUseCase.propertyFormEntity.agentName,
                            amenities = initTemporaryPropertyFormUseCase.propertyFormEntity.amenities,
                            pictureIds = initTemporaryPropertyFormUseCase.propertyFormEntity.pictures.map { it.id },
                            featuredPictureId = initTemporaryPropertyFormUseCase.propertyFormEntity.pictures.find { it.isFeatured }?.id,
                        )
                    }

                    Log.d(
                        "AddPropertyViewModel",
                        "initTemporaryPropertyFormUseCase with existing propertyForm: ${initTemporaryPropertyFormUseCase.propertyFormEntity}"
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
                            !form.price.isNullOrBlank() ||
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
                                form.price != null &&
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
                            price = form.price,
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
                    updatePropertyFormUseCase.invoke(
                        PropertyFormEntity(
                            type = it.propertyType,
                            price = it.price,
                            surface = it.surface,
                            description = it.description,
                            rooms = it.nbRooms,
                            bathrooms = it.nbBathrooms,
                            address = it.address,
                            bedrooms = it.nbBedrooms,
                            agentName = it.agent,
                            amenities = it.amenities.map { amenity ->
                                AmenityEntity(
                                    id = amenity.id,
                                    type = amenity.type,
                                )
                            },
                        )
                    )
                }
            }

            launch {
                getSavedPropertyFormEventUseCase.invoke().collectLatest {
                    formMutableStateFlow.map {
                        updatePropertyFormUseCase.invoke(
                            PropertyFormEntity(
                                type = it.propertyType,
                                price = it.price,
                                surface = it.surface,
                                description = it.description,
                                rooms = it.nbRooms,
                                bathrooms = it.nbBathrooms,
                                address = it.address,
                                bedrooms = it.nbBedrooms,
                                agentName = it.agent,
                                amenities = it.amenities.map { amenity ->
                                    AmenityEntity(
                                        id = amenity.id,
                                        type = amenity.type,
                                    )
                                },
                            )
                        )
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
                            perfectMatchPredictionMutableStateFlow.value = true
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


    private fun mapAmenityTypesToViewStates(amenityTypes: List<AmenityType>): List<AmenityViewState> {
        val viewStates = amenityTypes.map { amenityType ->
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
        return viewStates
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

    fun onPriceChanged(price: String?) {
        formMutableStateFlow.update {
            it.copy(price = price)
        }
    }

    fun onSurfaceChanged(surface: String?) {
        formMutableStateFlow.update {
            it.copy(surface = surface)
        }
    }

    fun onPictureSelected(stringUri: String) {
        viewModelScope.launch {
            val picturePath = saveFileToLocalAppFilesUseCase.invoke(stringUri)
            val addedPictureId =
                addPicturePreviewUseCase.invoke(picturePath, formMutableStateFlow.value.pictureIds.isEmpty())
            addPicturePreviewIdUseCase.invoke(addedPictureId)
            formMutableStateFlow.update {
                if (it.pictureIds.isEmpty()) {
                    it.copy(
                        pictureIds = listOf(addedPictureId),
                        featuredPictureId = addedPictureId
                    )
                } else {
                    it.copy(pictureIds = it.pictureIds + addedPictureId)
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

