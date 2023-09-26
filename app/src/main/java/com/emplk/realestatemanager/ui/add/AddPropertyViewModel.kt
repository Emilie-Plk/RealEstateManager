package com.emplk.realestatemanager.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.agent.GetAgentsFlowUseCase
import com.emplk.realestatemanager.domain.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.amenity.AmenityType
import com.emplk.realestatemanager.domain.amenity.type.GetAmenityTypeFlowUseCase
import com.emplk.realestatemanager.domain.autocomplete.GetAddressPredictionsUseCase
import com.emplk.realestatemanager.domain.locale_formatting.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.AddPropertyUseCase
import com.emplk.realestatemanager.domain.property_form.AddTemporaryPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_form.InitTemporaryPropertyFormUseCase
import com.emplk.realestatemanager.domain.property_form.PropertyFormDatabaseState
import com.emplk.realestatemanager.domain.property_form.PropertyFormEntity
import com.emplk.realestatemanager.domain.property_form.SetPropertyFormProgressUseCase
import com.emplk.realestatemanager.domain.property_form.UpdatePropertyFormUseCase
import com.emplk.realestatemanager.domain.property_form.location.LocationFormEntity
import com.emplk.realestatemanager.domain.property_form.picture_preview.AddPicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.DeletePicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.GetPicturePreviewsAsFlowUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.UpdatePicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.id.AddPicturePreviewIdUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.id.DeletePicturePreviewIdUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.id.GetPicturePreviewIdsAsFlowUseCase
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
import com.emplk.realestatemanager.ui.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.Clock
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Duration.Companion.seconds

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val addPropertyUseCase: AddPropertyUseCase,
    private val addTemporaryPropertyFormUseCase: AddTemporaryPropertyFormUseCase,
    private val deletePicturePreviewUseCase: DeletePicturePreviewUseCase,
    private val addPicturePreviewUseCase: AddPicturePreviewUseCase,
    private val addPicturePreviewIdUseCase: AddPicturePreviewIdUseCase,
    private val deletePicturePreviewIdUseCase: DeletePicturePreviewIdUseCase,
    private val getPicturePreviewIdsAsFlowUseCase: GetPicturePreviewIdsAsFlowUseCase,
    private val updatePicturePreviewUseCase: UpdatePicturePreviewUseCase,
    private val updatePropertyFormUseCase: UpdatePropertyFormUseCase,
    private val getAddressPredictionsUseCase: GetAddressPredictionsUseCase,
    private val initTemporaryPropertyFormUseCase: InitTemporaryPropertyFormUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase,
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
    private val getPicturePreviewsAsFlowUseCase: GetPicturePreviewsAsFlowUseCase,
    private val getAgentsFlowUseCase: GetAgentsFlowUseCase,
    private val setPropertyFormProgressUseCase: SetPropertyFormProgressUseCase,
    private val getAmenityTypeFlowUseCase: GetAmenityTypeFlowUseCase,
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
        val nbRooms: Int = 0,
        val nbBathrooms: Int = 0,
        val nbBedrooms: Int = 0,
        val agent: String? = null,
        val amenities: List<AmenityEntity> = emptyList(),
        val picturesCount: Int = 0,
    )

    private val formMutableStateFlow = MutableStateFlow(AddPropertyForm())

    private val currentAddressInputMutableStateFlow: MutableStateFlow<String?> = MutableStateFlow(null)
    private val currentPredictionAddressesFlow: Flow<List<String>> =
        currentAddressInputMutableStateFlow.transformLatest { input ->
            if (input.isNullOrBlank()) {
                emit(emptyList())
            } else {
                delay(400.milliseconds)
                emit(getAddressPredictionsUseCase.invoke(input))
            }
        }

    private val isEveryFieldFilledMutableStateFlow = MutableStateFlow(false)
    private val isAddingPropertyInDatabaseMutableStateFlow = MutableStateFlow(false)
    private val isPropertySuccessfullyAddedInDatabaseMutableSharedFlow =
        MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    private val onCreateButtonClickedMutableSharedFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val viewEventLiveData: LiveData<Event<AddPropertyViewEvent>> = liveData {
        onCreateButtonClickedMutableSharedFlow.collect {
            emit(Event(AddPropertyViewEvent.OnAddPropertyClicked))
            viewModelScope.launch(coroutineDispatcherProvider.io) {
                isAddingPropertyInDatabaseMutableStateFlow.value = true
                // Time to add property
                isAddingPropertyInDatabaseMutableStateFlow.value = false
                // isPropertySuccessfullyAddedInDatabaseMutableSharedFlow.tryEmit(success)
            }

            isPropertySuccessfullyAddedInDatabaseMutableSharedFlow.collect { success ->
                if (success) {
                    // Delete stored form
                    setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
                    emit(
                        Event(
                            AddPropertyViewEvent.ShowSnackBarPropertyCreated(
                                NativeText.Resource(R.string.add_property_successfully_created_snackBar_message)
                            )
                        )
                    )
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
                    formMutableStateFlow.update {
                        it.copy(
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
                        )
                    }
                }
            }

            launch {
                combine(
                    formMutableStateFlow,
                    getAgentsFlowUseCase.invoke(),
                    getPropertyTypeFlowUseCase.invoke(),
                    getAmenityTypeFlowUseCase.invoke(),
                    getPicturePreviewsAsFlowUseCase.invoke(),
                    getPicturePreviewIdsAsFlowUseCase.invoke(),
                    currentPredictionAddressesFlow,
                    isAddingPropertyInDatabaseMutableStateFlow,
                ) { form, agents, propertyTypes, amenityTypes, picturePreviews, picturePreviewIds, currentPredictionAddresses, isAddingPropertyInDatabase ->
                    val currencyType = getCurrencyTypeUseCase.invoke()

                    val isFormInProgress = !form.propertyType.isNullOrBlank() ||
                            !form.address.isNullOrBlank() ||
                            form.price > BigDecimal.ZERO ||
                            form.surface > 0 ||
                            !form.description.isNullOrBlank() ||
                            form.nbRooms > 0 ||
                            form.nbBathrooms > 0 ||
                            form.nbBedrooms > 0 ||
                            !form.agent.isNullOrBlank() ||
                            form.amenities.isNotEmpty() ||
                            form.picturesCount > 0
                    setPropertyFormProgressUseCase.invoke(isFormInProgress)
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
                                .filter { picturePreview -> picturePreviewIds.contains(picturePreview.id) }
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
                                            true
                                        } else {
                                            picturePreview.isFeatured
                                        },
                                        description = picturePreview.description,
                                        onDeleteEvent = EquatableCallback {
                                            formMutableStateFlow.update {
                                                it.copy(picturesCount = it.picturesCount - 1)
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
                            addressPredictions = currentPredictionAddresses.map { address ->
                                PredictionViewState.Prediction(
                                    address = address,
                                    onClickEvent = EquatableCallbackWithParam { prediction ->
                                        formMutableStateFlow.update {
                                            it.copy(
                                                address = prediction,
                                                addressPredictions = emptyList(),
                                            )
                                        }
                                    }
                                )
                            },
                        )
                    )
                }.collect()
            }

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

    fun onPictureAdded(uriToString: String) {
        viewModelScope.launch {
            val addedPictureId = addPicturePreviewUseCase.invoke(uriToString)
            addPicturePreviewIdUseCase.invoke(addedPictureId)
            formMutableStateFlow.update {
                it.copy(picturesCount = it.picturesCount + 1)
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
