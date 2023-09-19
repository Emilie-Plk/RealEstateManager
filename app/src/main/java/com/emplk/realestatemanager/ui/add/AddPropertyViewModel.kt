package com.emplk.realestatemanager.ui.add

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.agent.GetAgentsFlowUseCase
import com.emplk.realestatemanager.domain.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.amenity.AmenityType
import com.emplk.realestatemanager.domain.locale_formatting.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.location.LocationEntity
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.pictures.PictureEntity
import com.emplk.realestatemanager.domain.property.AddPropertyUseCase
import com.emplk.realestatemanager.domain.property.PropertyEntity
import com.emplk.realestatemanager.domain.property_form.picture_preview.AddPicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.DeletePicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.GetPicturePreviewFlowUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.PicturePreviewEntity
import com.emplk.realestatemanager.domain.property_form.picture_preview.UpdateFeaturedPictureUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.UpdatePicturePreviewDescriptionUseCase
import com.emplk.realestatemanager.domain.property_form.picture_preview.UpsertPicturePreviewUseCase
import com.emplk.realestatemanager.domain.property_type.GetPropertyTypeFlowUseCase
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentViewStateItem
import com.emplk.realestatemanager.ui.add.picture_preview.PicturePreviewStateItem
import com.emplk.realestatemanager.ui.add.type.AddPropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val addPropertyUseCase: AddPropertyUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase,
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
    private val getAgentsFlowUseCase: GetAgentsFlowUseCase,
    private val addPicturePreviewUseCase: AddPicturePreviewUseCase,
    private val upsertPicturePreviewUseCase: UpsertPicturePreviewUseCase,
    private val updatePicturePreviewDescriptionUseCase: UpdatePicturePreviewDescriptionUseCase,
    private val updateFeaturedPictureUseCase: UpdateFeaturedPictureUseCase,
    private val getPicturePreviewUseCase: GetPicturePreviewFlowUseCase,
    private val deletePicturePreviewUseCase: DeletePicturePreviewUseCase,
    private val getPropertyTypeFlowUseCase: GetPropertyTypeFlowUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    private data class AddPropertyForm(
        val propertyType: String? = null,
        val address: String? = null,
        val price: BigDecimal = BigDecimal.ZERO,
        val surface: String? = null,
        val description: String? = null,
        val nbRooms: Int = 0,
        val nbBathrooms: Int = 0,
        val nbBedrooms: Int = 0,
        val amenities: List<String> = emptyList(),
        val agent: String? = null,
        val pictures: List<PicturePreviewStateItem.AddPropertyPicturePreview> = emptyList(),
    )

    private val formMutableStateFlow = MutableStateFlow(AddPropertyForm())

    private val isEveryFieldFilledMutableStateFlow = MutableStateFlow(false)
    private val isAddingPropertyInDatabaseMutableStateFlow = MutableStateFlow(false)
    private val picturePreviewsMutableStateFlow =
        MutableStateFlow<List<PicturePreviewStateItem.AddPropertyPicturePreview>>(emptyList())
    private val isPropertySuccessfullyAddedInDatabaseMutableSharedFlow =
        MutableSharedFlow<Boolean>(extraBufferCapacity = 1)
    private val onCreateButtonClickedMutableSharedFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    val viewEventLiveData: LiveData<Event<AddPropertyViewEvent>> = liveData {
        onCreateButtonClickedMutableSharedFlow.collect {
            emit(Event(AddPropertyViewEvent.OnAddPropertyClicked))
            viewModelScope.launch(coroutineDispatcherProvider.io) {
                isAddingPropertyInDatabaseMutableStateFlow.value = true
                val success = addPropertyUseCase.invoke(
                    PropertyEntity(
                        type = "Villa",
                        price = BigDecimal(15234574845),
                        surface = 326565,
                        rooms = 10,
                        bedrooms = 5,
                        bathrooms = 5,
                        description = "Splendid villa with swimming pool and garden",
                        agentName = "Michel",
                        isAvailableForSale = true,
                        isSold = false,
                        entryDate = LocalDateTime.now(),
                        saleDate = null,
                        amenities = listOf(
                            AmenityEntity(
                                type = AmenityType.PARK,
                                propertyId = 0,
                            ),
                            AmenityEntity(
                                type = AmenityType.SCHOOL,
                                propertyId = 0,
                            ),
                            AmenityEntity(
                                type = AmenityType.GYM,
                                propertyId = 0,
                            )
                        ),
                        pictures = listOf(
                            PictureEntity(
                                uri = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dmlsbGF8ZW58MHx8MHx8fDA%3D&w=300&q=300",
                                description = "Villa",
                                isFeatured = true,
                                propertyId = 0,
                            ),
                            PictureEntity(
                                uri = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dmlsbGF8ZW58MHx8MHx8fDA%3D&w=300&q=300",
                                description = "Villa",
                                isFeatured = false,
                                propertyId = 0,
                            ),
                        ),
                        location = LocationEntity(
                            propertyId = 0,
                            address = "1 rue de la paix",
                            postalCode = "75000",
                            city = "Paris",
                            latitude = 48.8566,
                            longitude = 2.3522,
                        )
                    )
                )
                isAddingPropertyInDatabaseMutableStateFlow.value = false
                isPropertySuccessfullyAddedInDatabaseMutableSharedFlow.tryEmit(success)
            }

            isPropertySuccessfullyAddedInDatabaseMutableSharedFlow.collect { success ->
                if (success) {
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
        combine(
            formMutableStateFlow,
            getAgentsFlowUseCase.invoke(),
            getPropertyTypeFlowUseCase.invoke(),
            getPicturePreviewUseCase.invoke(),
            isAddingPropertyInDatabaseMutableStateFlow,
        ) { form, agents, propertyTypes, picturePreviews, isAddingPropertyInDatabase ->
            val currencyType = getCurrencyTypeUseCase.invoke()
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
                    amenities = form.amenities,
                    pictures = mapToPicturePreviewStateItems(picturePreviews), // Cannot sort by now
                    agent = form.agent,
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
                )
            )
        }.collect()
    }

    private fun mapToPicturePreviewStateItems(picturePreviews: List<PicturePreviewEntity>): List<PicturePreviewStateItem.AddPropertyPicturePreview> {
        return picturePreviews.map { picturePreview ->
            PicturePreviewStateItem.AddPropertyPicturePreview(
                id = picturePreview.id,
                uri = NativePhoto.Uri(picturePreview.uri),
                description = picturePreview.description,
                isFeatured = picturePreview.isFeatured,
                onDescriptionChanged = EquatableCallback {
                   viewModelScope.launch {
                        updatePicturePreviewDescriptionUseCase.invoke(picturePreview.id, picturePreview.description)
                    }
                },
                onDeleteEvent = EquatableCallback {
                    viewModelScope.launch {
                        deletePicturePreviewUseCase.invoke(picturePreview.id)
                    }
                },
                onFeaturedEvent = EquatableCallback {
                    viewModelScope.launch {
                        // Clear the isFeatured flag for all pictures
                        picturePreviews.forEach { picture ->
                            updateFeaturedPictureUseCase.invoke(picture.id, false)
                        }

                        // Set the isFeatured flag to true for the clicked picturePreview
                        updateFeaturedPictureUseCase.invoke(picturePreview.id, true)
                    }
                },

                )
        }
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

    fun onAddressChanged(address: String) {
        formMutableStateFlow.update {
            it.copy(address = address)
        }
    }

    fun onPriceChanged(price: String) {
        formMutableStateFlow.update {
            it.copy(price = price.toBigDecimal())
        }
    }

    fun onSurfaceChanged(surface: String) {
        formMutableStateFlow.update {
            it.copy(surface = surface)
        }
    }

    fun onPictureSelected(uri: Uri) {
        viewModelScope.launch {
            addPicturePreviewUseCase.invoke(
                PicturePreviewEntity(
                    uri = uri.toString(),
                    description = "",
                    isFeatured = false,
                )
            )
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

    fun onAmenityAdded(amenity: String) {
        val currentAmenities = formMutableStateFlow.value.amenities.toMutableSet()
        if (currentAmenities.contains(amenity)) {
            currentAmenities.remove(amenity)
        } else {
            currentAmenities.add(amenity)
        }
        formMutableStateFlow.update {
            it.copy(amenities = currentAmenities.toList())
        }
    }

    fun onAddPropertyClicked() {
        onCreateButtonClickedMutableSharedFlow.tryEmit(Unit)
    }
}
