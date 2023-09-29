package com.emplk.realestatemanager.ui.add

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.agent.GetAgentsFlowUseCase
import com.emplk.realestatemanager.domain.autocomplete.GetAddressPredictionsUseCase
import com.emplk.realestatemanager.domain.autocomplete.PredictionWrapper
import com.emplk.realestatemanager.domain.geocoding.GeocodingWrapper
import com.emplk.realestatemanager.domain.geocoding.GetAddressLatLongUseCase
import com.emplk.realestatemanager.domain.locale_formatting.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.map_picture.GetMapPictureUseCase
import com.emplk.realestatemanager.domain.map_picture.MapWrapper
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.AddPropertyUseCase
import com.emplk.realestatemanager.domain.property.PropertyEntity
import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property.amenity.type.GetAmenityTypeUseCase
import com.emplk.realestatemanager.domain.property.location.LocationEntity
import com.emplk.realestatemanager.domain.property.pictures.PictureEntity
import com.emplk.realestatemanager.domain.property_form.AddTemporaryPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_form.DeleteTemporaryPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_form.GetSavedPropertyFormEventUseCase
import com.emplk.realestatemanager.domain.property_form.InitTemporaryPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_form.PropertyFormDatabaseState
import com.emplk.realestatemanager.domain.property_form.PropertyFormEntity
import com.emplk.realestatemanager.domain.property_form.SetPropertyFormProgressUseCase
import com.emplk.realestatemanager.domain.property_form.UpdatePropertyFormUseCase
import com.emplk.realestatemanager.domain.property_form.location.LocationFormEntity
import com.emplk.realestatemanager.domain.property_form.picture_preview.AddPicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.GetPicturePreviewsAsFlowUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.GetPicturePreviewsUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.SavePictureFromGalleryToAppFilesUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.UpdatePicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.id.AddPicturePreviewIdUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.id.DeleteAllPicturePreviewIdsUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.id.DeletePicturePreviewIdUseCase
import com.emplk.realestatemanager.domain.property_type.GetPropertyTypeFlowUseCase
import com.emplk.realestatemanager.ui.add.address_predictions.PredictionViewState
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentViewStateItem
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewStateItem
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
import java.time.Clock
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val addPropertyUseCase: AddPropertyUseCase,
    private val addTemporaryPropertyFormUseCase: AddTemporaryPropertyFormUseCase,
    private val getSavedPropertyFormEventUseCase: GetSavedPropertyFormEventUseCase,
    private val deleteTemporaryPropertyFormUseCase: DeleteTemporaryPropertyFormUseCase,
    private val addPicturePreviewUseCase: AddPicturePreviewUseCase,
    private val addPicturePreviewIdUseCase: AddPicturePreviewIdUseCase,
    private val savePictureFromGalleryToAppFilesUseCase: SavePictureFromGalleryToAppFilesUseCase,
    private val getPicturePreviewsUseCase: GetPicturePreviewsUseCase,
    private val deletePicturePreviewIdUseCase: DeletePicturePreviewIdUseCase,
    private val deleteAllPicturePreviewIdsUseCase: DeleteAllPicturePreviewIdsUseCase,
    private val getMapPictureUseCase: GetMapPictureUseCase,
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
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
    private val clock: Clock,
) : ViewModel() {

    private data class AddPropertyForm(
        val propertyType: String? = null,
        val address: String? = null,
        val addressPredictions: List<PredictionViewState> = emptyList(),
        val lat: String? = null,
        val lng: String? = null,
        val price: BigDecimal = BigDecimal.ZERO,
        val surface: Int = 0,
        val description: String? = null,
        val nbRooms: Int = 0,  // TODO: change to Int? = null ?
        val nbBathrooms: Int = 0,
        val nbBedrooms: Int = 0,
        val agent: String? = null,
        val amenities: List<AmenityEntity> = emptyList(),
        val picturesId: List<Long> = emptyList(),
    )

    private val formMutableStateFlow = MutableStateFlow(AddPropertyForm())

    private val currentAddressInputMutableStateFlow: MutableStateFlow<String?> = MutableStateFlow(null)
    private val currentPredictionAddressesFlow: Flow<PredictionWrapper?> =
        currentAddressInputMutableStateFlow.transformLatest { input ->
            if (input.isNullOrBlank() || input.length < 3) {
                emit(null)
            } else {
                delay(400.milliseconds)
                emit(getAddressPredictionsUseCase.invoke(input))
            }
        }

    private val isEveryFieldFilledMutableStateFlow = MutableStateFlow(false)
    private val isAddingPropertyInDatabaseMutableStateFlow = MutableStateFlow(false)
    private val onCreateButtonClickedMutableSharedFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val viewEventLiveData: LiveData<Event<AddPropertyEvent>> = liveData {
        onCreateButtonClickedMutableSharedFlow.collect {
            viewModelScope.launch(coroutineDispatcherProvider.io) {
                isAddingPropertyInDatabaseMutableStateFlow.value = true
                formMutableStateFlow.collectLatest { form ->
                    if (form.propertyType != null &&
                        form.address != null &&
                        form.lat != null &&
                        form.lng != null &&
                        form.price > BigDecimal.ZERO &&
                        form.surface > 0 &&
                        form.description != null &&
                        form.nbRooms > 0 &&
                        form.nbBathrooms > 0 &&
                        form.nbBedrooms > 0 &&
                        form.agent != null &&
                        form.amenities.isNotEmpty() &&
                        form.picturesId.isNotEmpty()
                    ) {
                        Log.d("COUCOU", "entering form collect: $form")
                        val success = addPropertyUseCase.invoke(
                            PropertyEntity(
                                type = form.propertyType,
                                price = form.price,
                                surface = form.surface,
                                description = form.description,
                                rooms = form.nbRooms,
                                bathrooms = form.nbBathrooms,
                                location = LocationEntity(
                                    address = form.address,
                                    latitude = form.lat,
                                    longitude = form.lng,
                                    miniatureMapPath = when (val mapWrapper =
                                        getMapPictureUseCase.invoke(form.lat, form.lng)) {
                                        is MapWrapper.Success -> mapWrapper.mapPicture
                                        else -> "Error"
                                    },
                                ),
                                bedrooms = form.nbBedrooms,
                                agentName = form.agent,
                                amenities = form.amenities,
                                pictures = getPicturePreviewsUseCase.invoke().map {
                                    PictureEntity(
                                        uri = it.uri,
                                        description = it.description ?: "",
                                        isFeatured = it.isFeatured,
                                    )
                                },
                                entryDate = LocalDateTime.now(clock),
                                isAvailableForSale = false,
                                isSold = false,
                                saleDate = null,
                            ),
                        )
                        isAddingPropertyInDatabaseMutableStateFlow.value = false
                        if (success) {
                            val deletionSuccess = deleteTemporaryPropertyFormUseCase.invoke()
                            if (deletionSuccess != null && deletionSuccess) {
                                deleteAllPicturePreviewIdsUseCase.invoke()
                            }
                            setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                            emit(
                                Event(
                                    AddPropertyEvent.ShowToast(
                                        NativeText.Resource(R.string.add_property_successfully_created_snackBar_message)
                                    )
                                )
                            )
                        } else {
                            emit(
                                Event(
                                    AddPropertyEvent.ShowToast(
                                        NativeText.Resource(R.string.add_property_error_message)
                                    )
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    val viewStateLiveData: LiveData<AddPropertyViewState> = liveData {
        coroutineScope {
            when (val initTemporaryPropertyFormUseCase = initTemporaryPropertyFormUseCase.invoke()) {
                is PropertyFormDatabaseState.Empty -> {
                }

                is PropertyFormDatabaseState.DraftAlreadyExists -> {
                    formMutableStateFlow.update { addPropertyForm ->
                        addPropertyForm.copy(
                            propertyType = initTemporaryPropertyFormUseCase.propertyFormEntity.type,
                            address = initTemporaryPropertyFormUseCase.propertyFormEntity.location?.address,
                            lat = initTemporaryPropertyFormUseCase.propertyFormEntity.location?.latitude,
                            lng = initTemporaryPropertyFormUseCase.propertyFormEntity.location?.longitude,
                            price = initTemporaryPropertyFormUseCase.propertyFormEntity.price ?: BigDecimal.ZERO,
                            surface = initTemporaryPropertyFormUseCase.propertyFormEntity.surface ?: 0,
                            description = initTemporaryPropertyFormUseCase.propertyFormEntity.description,
                            nbRooms = initTemporaryPropertyFormUseCase.propertyFormEntity.rooms ?: 0,
                            nbBathrooms = initTemporaryPropertyFormUseCase.propertyFormEntity.bathrooms ?: 0,
                            nbBedrooms = initTemporaryPropertyFormUseCase.propertyFormEntity.bedrooms ?: 0,
                            agent = initTemporaryPropertyFormUseCase.propertyFormEntity.agentName,
                            amenities = initTemporaryPropertyFormUseCase.propertyFormEntity.amenities,
                            picturesId = initTemporaryPropertyFormUseCase.propertyFormEntity.pictures.map { it.id },
                        )
                    }
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
                            form.price > BigDecimal.ZERO ||
                            form.surface > 0 ||
                            !form.description.isNullOrBlank() ||
                            form.nbRooms > 0 ||
                            form.lat != null ||
                            form.lng != null ||
                            form.nbBathrooms > 0 ||
                            form.nbBedrooms > 0 ||
                            !form.agent.isNullOrBlank() ||
                            form.amenities.isNotEmpty() ||
                            form.picturesId.isNotEmpty()

                    setPropertyFormProgressUseCase.invoke(isFormInProgress)
                    isEveryFieldFilledMutableStateFlow.value = form.propertyType != null &&
                            form.address != null &&
                            form.lat != null &&
                            form.lng != null &&
                            form.price > BigDecimal.ZERO &&
                            form.surface > 0 &&
                            form.description != null &&
                            form.nbRooms > 0 &&
                            form.nbBathrooms > 0 &&
                            form.nbBedrooms > 0 &&
                            form.agent != null &&
                            form.amenities.isNotEmpty() &&
                            form.picturesId.isNotEmpty()

                    emit(
                        AddPropertyViewState(
                            propertyType = form.propertyType,
                            address = form.address,
                            lat = form.lat,
                            lng = form.lng,
                            price = form.price.toString(),
                            surface = form.surface.toString(),
                            description = form.description,
                            nbRooms = form.nbRooms,
                            nbBathrooms = form.nbBathrooms,
                            nbBedrooms = form.nbBedrooms,
                            pictures = picturePreviews
                                .filter { picturePreview -> form.picturesId.contains(picturePreview.id) }
                                .map { picturePreview ->
                                    PicturePreviewStateItem.AddPropertyPicturePreview(
                                        id = picturePreview.id,
                                        uri = picturePreview.uri,
                                        isFeatured = if (picturePreviews.size == 1 && picturePreviews[0].id == picturePreview.id) {
                                            viewModelScope.launch {
                                                updatePicturePreviewUseCase.invoke(
                                                    picturePreviews[0].id,
                                                    true,
                                                    picturePreview.description
                                                )
                                            }
                                            true  // TODO: remove or change this
                                        } else {
                                            picturePreview.isFeatured
                                        },
                                        description = picturePreview.description,
                                        onDeleteEvent = EquatableCallback {
                                            formMutableStateFlow.update {
                                                it.copy(picturesId = it.picturesId.filter { id -> id != picturePreview.id })
                                            }
                                            deletePicturePreviewIdUseCase.invoke(picturePreview.id)
                                        },
                                        onFeaturedEvent = EquatableCallbackWithParam { isFeatured ->
                                            if (picturePreviews[0].id != picturePreview.id) {
                                                viewModelScope.launch {
                                                    updatePicturePreviewUseCase.invoke(
                                                        picturePreview.id,
                                                        isFeatured,
                                                        picturePreview.description
                                                    )
                                                    updatePicturePreviewUseCase.invoke(
                                                        picturePreviews[0].id,
                                                        false,
                                                        picturePreview.description
                                                    )
                                                }
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
                            location = LocationFormEntity(
                                address = it.address,
                                latitude = it.lat,
                                longitude = it.lng,
                            ),
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
                                location = LocationFormEntity(
                                    address = it.address,
                                    latitude = it.lat,
                                    longitude = it.lng,
                                ),
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
                            viewModelScope.launch {

                                when (val wrapper = getAddressLatLongUseCase.invoke(selectedAddress)) {
                                    is GeocodingWrapper.Success -> {
                                        formMutableStateFlow.update {
                                            it.copy(
                                                address = selectedAddress,
                                                lat = wrapper.result.lat,
                                                lng = wrapper.result.lng,
                                                addressPredictions = emptyList()
                                            )
                                        }
                                    }

                                    is GeocodingWrapper.Error -> Unit
                                    GeocodingWrapper.NoResult -> Unit
                                }
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


    private fun mapAmenityTypesToViewStates(amenityTypes: List<AmenityType>): List<AmenityViewStateItem> {
        val viewStates = amenityTypes.map { amenityType ->
            AmenityViewStateItem(
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

    fun onAddressChanged(input: String) {

        currentAddressInputMutableStateFlow.value = input
    }

    fun onPriceChanged(price: String) {
        formMutableStateFlow.update {
            if (price.isBlank()) {
                it.copy(price = BigDecimal.ZERO)
            } else {
                it.copy(price = price.toBigDecimal())
            }
        }
    }

    fun onSurfaceChanged(surface: String) {
        formMutableStateFlow.update {
            it.copy(surface = surface.toIntOrNull() ?: 0)
        }
    }

    fun onPictureFromCameraTaken(uriToString: String) {
        viewModelScope.launch {
            val addedPictureId = addPicturePreviewUseCase.invoke(uriToString)
            addPicturePreviewIdUseCase.invoke(addedPictureId)
            formMutableStateFlow.update {
                it.copy(picturesId = it.picturesId + addedPictureId)
            }
        }
    }

    fun onPictureFromGallerySelected(uri: Uri) {
        viewModelScope.launch {
            val picturePath = savePictureFromGalleryToAppFilesUseCase.invoke(uri)
            val addedPictureId = addPicturePreviewUseCase.invoke(picturePath ?: "") // TODO: manage case fail
            addPicturePreviewIdUseCase.invoke(addedPictureId)
            formMutableStateFlow.update {
                it.copy(picturesId = it.picturesId + addedPictureId)
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
}
