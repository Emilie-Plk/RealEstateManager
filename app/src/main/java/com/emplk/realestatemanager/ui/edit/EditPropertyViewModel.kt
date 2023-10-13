package com.emplk.realestatemanager.ui.edit

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.domain.agent.GetAgentsMapUseCase
import com.emplk.realestatemanager.domain.autocomplete.GetAddressPredictionsUseCase
import com.emplk.realestatemanager.domain.autocomplete.PredictionWrapper
import com.emplk.realestatemanager.domain.current_property.GetCurrentPropertyIdFlowUseCase
import com.emplk.realestatemanager.domain.locale_formatting.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.property.GetPropertyByItsIdAsFlowUseCase
import com.emplk.realestatemanager.domain.property.UpdatePropertyUseCase
import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property.amenity.type.GetAmenityTypeUseCase
import com.emplk.realestatemanager.domain.property.pictures.PictureEntity
import com.emplk.realestatemanager.domain.property_draft.PropertyFormStateEntity
import com.emplk.realestatemanager.domain.property_type.GetPropertyTypeFlowUseCase
import com.emplk.realestatemanager.ui.add.PropertyFormViewState
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
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject
import kotlin.time.Duration.Companion.milliseconds

@HiltViewModel
class EditPropertyViewModel @Inject constructor(
    private val updatePropertyUseCase: UpdatePropertyUseCase,
    private val getPropertyByItsIdAsFlowUseCase: GetPropertyByItsIdAsFlowUseCase,
    private val getCurrentPropertyIdFlowUseCase: GetCurrentPropertyIdFlowUseCase,
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase,
    private val getAmenityTypeUseCase: GetAmenityTypeUseCase,
    private val getAgentsMapUseCase: GetAgentsMapUseCase,
    private val getAddressPredictionsUseCase: GetAddressPredictionsUseCase,
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
    private val getPropertyTypeFlowUseCase: GetPropertyTypeFlowUseCase,
) : ViewModel() {

    private val formMutableStateFlow = MutableStateFlow(PropertyFormStateEntity())
    private val isUpdatingPropertyInDatabaseMutableStateFlow = MutableStateFlow(false)
    private val isEveryFieldFilledMutableStateFlow = MutableStateFlow(false)
    private val onUpdateButtonClickedMutableSharedFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    private val perfectMatchPredictionMutableStateFlow = MutableStateFlow<Boolean?>(null)
    private val hasAddressEditTextFocus = MutableStateFlow(false)
    private val currentAddressInputMutableStateFlow: MutableStateFlow<String?> = MutableStateFlow(null)


    private val currentPredictionAddressesFlow: Flow<PredictionWrapper?> =
        currentAddressInputMutableStateFlow.transformLatest { input ->
            if (input.isNullOrBlank() || input.length < 3 || perfectMatchPredictionMutableStateFlow.value == true || hasAddressEditTextFocus.value.not()) {
                emit(null)
            } else {
                delay(400.milliseconds)
                emit(getAddressPredictionsUseCase.invoke(input))
            }
        }

    val viewEventLiveData: LiveData<Event<EditPropertyEvent>> = liveData {
        onUpdateButtonClickedMutableSharedFlow.collect {
            formMutableStateFlow.collect { form ->
                isUpdatingPropertyInDatabaseMutableStateFlow.tryEmit(true)
                val resultEvent = updatePropertyUseCase.invoke(form)
                emit(Event(resultEvent))
                isUpdatingPropertyInDatabaseMutableStateFlow.tryEmit(false)
            }
        }
    }

    val viewStateLiveData: LiveData<PropertyFormViewState> = liveData {
        coroutineScope {
            getCurrentPropertyIdFlowUseCase.invoke()
                .filterNotNull()
                .flatMapLatest { propertyId ->
                    getPropertyByItsIdAsFlowUseCase.invoke(propertyId)
                }.collectLatest { property ->

                    formMutableStateFlow.update { propertyForm ->
                        propertyForm.copy(
                            propertyType = property.type,
                            address = property.location.address,
                            price = property.price,
                            surface = property.surface.toString(),
                            description = property.description,
                            nbRooms = property.rooms,
                            nbBathrooms = property.bathrooms,
                            nbBedrooms = property.bedrooms,
                            amenities = property.amenities,
                            pictureIds = property.pictures.map { picture -> picture.id },
                            featuredPictureId = property.pictures.find { picture -> picture.isFeatured }?.id,
                            agent = property.agentName,
                        )
                    }

                    val currencyType = getCurrencyTypeUseCase.invoke()
                    val amenityTypes = getAmenityTypeUseCase.invoke()
                    val propertyTypes = getPropertyTypeFlowUseCase.invoke()
                    val agents = getAgentsMapUseCase.invoke()

                    launch {
                        combine(
                            formMutableStateFlow,
                            currentPredictionAddressesFlow,
                            isUpdatingPropertyInDatabaseMutableStateFlow,
                        ) { form, addressPredictions, isUpdating ->

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
                                PropertyFormViewState(
                                    propertyType = form.propertyType,
                                    addressPredictions = mapPredictionsToViewState(addressPredictions),
                                    address = form.address,
                                    isAddressValid = true, // TODO: change that of course
                                    price = if (form.price == BigDecimal.ZERO) "" else form.price.toString(),
                                    surface = form.surface,
                                    description = form.description,
                                    nbRooms = form.nbRooms,
                                    nbBathrooms = form.nbBathrooms,
                                    nbBedrooms = form.nbBedrooms,
                                    selectedAmenities = form.amenities,
                                    amenities = mapAmenityTypesToViewStates(amenityTypes, form.amenities),
                                    pictures = mapPicturesToViewStates(
                                        property.pictures
                                    ),
                                    agents = agents.map { agent ->
                                        AddPropertyAgentViewStateItem(
                                            id = agent.key,
                                            name = agent.value
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
                                    isSubmitButtonEnabled = isEveryFieldFilledMutableStateFlow.value,
                                    isProgressBarVisible = isUpdating,
                                    propertyTypes = propertyTypes.map { propertyType ->
                                        AddPropertyTypeViewStateItem(
                                            id = propertyType.key,
                                            name = propertyType.value,
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
                            perfectMatchPredictionMutableStateFlow.tryEmit(true)
                            currentAddressInputMutableStateFlow.tryEmit(null)
                            formMutableStateFlow.update {
                                it.copy(
                                    address = selectedAddress,
                                   // addressPredictions = emptyList(),
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

    private fun mapPicturesToViewStates(pictures: List<PictureEntity>): List<PicturePreviewStateItem> =
        pictures.map { pictureEntity ->
            PicturePreviewStateItem.EditPropertyPicturePreview(
                id = pictureEntity.id,
                uri = pictureEntity.uri,
                description = pictureEntity.description,
                isFeatured = pictureEntity.isFeatured,
                onDeleteEvent = EquatableCallback {
                },
                onFeaturedEvent = EquatableCallbackWithParam { isFeatured ->
                    if (pictureEntity.isFeatured) return@EquatableCallbackWithParam
                },
                onDescriptionChanged = EquatableCallbackWithParam { description ->

                },
            )
        }

    private fun mapAmenityTypesToViewStates(
        amenityTypes: List<AmenityType>,
        selectedAmenities: List<AmenityEntity>
    ): List<AmenityViewState> =
        amenityTypes.map { amenityType ->
            AmenityViewState.AmenityCheckbox(
                id = amenityType.id,
                name = amenityType.name,
                isChecked = selectedAmenities.any { amenity -> amenity.id == amenityType.id },
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

    fun onPropertyTypeChanged(propertyType: String) {
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
                   // addressPredictions = emptyList(),
                )
            }
        } else if (perfectMatchPredictionMutableStateFlow.value == true) {
            currentAddressInputMutableStateFlow.tryEmit(input)
            formMutableStateFlow.update {
                it.copy(
                    address = input,
              //      addressPredictions = emptyList(),
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

    /* fun onPictureSelected(stringUri: String) {
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
     }*/

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

    fun onUpdatePropertyClicked() {
        onUpdateButtonClickedMutableSharedFlow.tryEmit(Unit)
    }

    fun onAddressEditTextFocused(hasFocus: Boolean) {
        hasAddressEditTextFocus.tryEmit(hasFocus)
    }
}