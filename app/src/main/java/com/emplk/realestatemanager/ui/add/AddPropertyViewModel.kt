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
import com.emplk.realestatemanager.domain.navigation.GetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.pictures.PictureEntity
import com.emplk.realestatemanager.domain.property.AddPropertyUseCase
import com.emplk.realestatemanager.domain.property.PropertyEntity
import com.emplk.realestatemanager.domain.property_type.GetPropertyTypeFlowUseCase
import com.emplk.realestatemanager.domain.screen_width.GetScreenWidthTypeFlowUseCase
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentViewStateItem
import com.emplk.realestatemanager.ui.add.picture_preview.PicturePreviewStateItem
import com.emplk.realestatemanager.ui.add.type.AddPropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.EquatableCallback
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativePhoto
import com.emplk.realestatemanager.ui.utils.NativeText
import com.emplk.realestatemanager.ui.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val addPropertyUseCase: AddPropertyUseCase,
    private val getNavigationTypeUseCase: GetNavigationTypeUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val getScreenWidthTypeFlowUseCase: GetScreenWidthTypeFlowUseCase,
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase,
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
    private val getAgentsFlowUseCase: GetAgentsFlowUseCase,
    private val getPropertyTypeFlowUseCase: GetPropertyTypeFlowUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    private val propertyTypeMutableStateFlow = MutableStateFlow<String?>(null)
    private val addressMutableStateFlow = MutableStateFlow<String?>(null)
    private val priceMutableStateFlow = MutableStateFlow<String?>(null)
    private val surfaceMutableStateFlow = MutableStateFlow<String?>(null)
    private val descriptionMutableStateFlow = MutableStateFlow<String?>(null)
    private val nbRoomsMutableStateFlow = MutableStateFlow<String?>(null)
    private val nbBathroomsMutableStateFlow = MutableStateFlow<String?>(null)
    private val nbBedroomsMutableStateFlow = MutableStateFlow<String?>(null)
    private val amenitiesMutableStateFlow = MutableStateFlow<List<String>>(emptyList())
    private val agentMutableStateFlow = MutableStateFlow<String?>(null)
    private val urisPictureFromStorageMutableStateFLow = MutableStateFlow<List<Uri?>>(emptyList())
    private val pictureMutableStateFlow =
        MutableStateFlow<List<PicturePreviewStateItem.AddPropertyPicturePreview>>(emptyList())
    private val isEveryFieldFilledMutableStateFlow = MutableStateFlow(false)
    private val onCreateButtonClickedMutableSharedFlow = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    private val isAddingPropertyInDatabaseMutableStateFlow = MutableStateFlow(false)
    private val isPropertySuccessfullyAddedInDatabaseMutableSharedFlow =
        MutableSharedFlow<Boolean>(extraBufferCapacity = 1)

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

    val addPropertyViewStateLiveData: LiveData<AddPropertyViewStateItem> = liveData {
        combine(
            propertyTypeMutableStateFlow,
            addressMutableStateFlow,
            priceMutableStateFlow,
            surfaceMutableStateFlow,
            descriptionMutableStateFlow,
            nbRoomsMutableStateFlow,
            nbBathroomsMutableStateFlow,
            nbBedroomsMutableStateFlow,
            amenitiesMutableStateFlow,
            agentMutableStateFlow,
            pictureMutableStateFlow,
        ) { propertyType, address, price, surface, description, nbRooms, nbBathrooms, nbBedrooms, amenities, agent, pictures ->
            val isFormValid = (propertyType != null &&
                    address.isNullOrBlank() &&
                    price.isNullOrBlank() &&
                    surface.isNullOrBlank() &&
                    description.isNullOrBlank() &&
                    nbRooms.isNullOrBlank() &&
                    nbBathrooms.isNullOrBlank() &&
                    nbBedrooms.isNullOrBlank() &&
                    agent != null &&
                    pictures.isNotEmpty())

            isEveryFieldFilledMutableStateFlow.value = isFormValid

            emit(
                AddPropertyViewStateItem(
                    propertyType = propertyType ?: "",
                    address = address ?: "",
                    price = price ?: "",
                    surface = surface ?: "",
                    description = description ?: "",
                    nbRooms = nbRooms ?: "",
                    nbBathrooms = nbBathrooms ?: "",
                    nbBedrooms = nbBedrooms ?: "",
                    amenities = amenities,
                    agent = agent ?: "",
                    pictures = pictures.map { picture ->
                        PicturePreviewStateItem.AddPropertyPicturePreview(
                            id = picture.id,
                            uri = picture.uri,
                            description = picture.description,
                            isFeatured = picture.isFeatured,
                            onDeleteEvent = EquatableCallback { },
                            onFeaturedEvent = EquatableCallback { },
                            onDescriptionChanged = EquatableCallback { },
                        )
                    },
                )
            )
        }.collect()
    }

    val viewStateLiveData: LiveData<AddPropertyViewState> = liveData {
        combine(
            getAgentsFlowUseCase.invoke(),
            getPropertyTypeFlowUseCase.invoke(),
            isAddingPropertyInDatabaseMutableStateFlow.asStateFlow(),
        ) { agents, propertyTypes, isAddingPropertyInDatabase ->
            val currencyType = getCurrencyTypeUseCase.invoke()
            emit(
                AddPropertyViewState(
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
                            name = agent.value,
                        )
                    },
                )
            )
        }.collect()
    }

    fun onPropertyTypeSelected(propertyType: String) {
        propertyTypeMutableStateFlow.value = propertyType
    }

    fun onAgentSelected(agent: String) {
        agentMutableStateFlow.value = agent
    }

    fun onAddressChanged(address: String) {
        addressMutableStateFlow.value = address
    }

    fun onPriceChanged(price: String) {
        priceMutableStateFlow.value = price
    }

    fun onSurfaceChanged(surface: String) {
        surfaceMutableStateFlow.value = surface
    }

    fun onPictureFromStorageSelected(uri: Uri) {
        pictureMutableStateFlow.update { pictures ->
            val pictureId = 0L
            pictures + PicturePreviewStateItem.AddPropertyPicturePreview(
                id = pictureId + 1L,
                uri = NativePhoto.Uri(uri.toString()),
                description = "",
                isFeatured = pictures.isEmpty(),
                onDeleteEvent = EquatableCallback { },
                onFeaturedEvent = EquatableCallback { },
                onDescriptionChanged = EquatableCallback { },
            )
        }
    }

    fun onAddPropertyClicked() {
        onCreateButtonClickedMutableSharedFlow.tryEmit(Unit)
    }

    fun onRoomsNumberChanged(value: Int) {

    }

    fun onBedroomsNumberChanged(value: Int) {

    }

    fun onBathroomsNumberChanged(value: Int) {

    }
}
