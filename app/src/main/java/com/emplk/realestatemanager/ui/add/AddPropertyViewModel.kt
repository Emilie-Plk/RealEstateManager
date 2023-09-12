package com.emplk.realestatemanager.ui.add

import android.util.Log
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
import com.emplk.realestatemanager.domain.pictures.PictureEntity
import com.emplk.realestatemanager.domain.property.AddPropertyUseCase
import com.emplk.realestatemanager.domain.property.PropertyEntity
import com.emplk.realestatemanager.domain.property_type.GetPropertyTypeFlowUseCase
import com.emplk.realestatemanager.domain.screen_width.GetScreenWidthTypeFlowUseCase
import com.emplk.realestatemanager.ui.AddPropertyPictureStateItem
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentViewStateItem
import com.emplk.realestatemanager.ui.add.type.AddPropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.NativeText
import com.emplk.realestatemanager.ui.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val addPropertyUseCase: AddPropertyUseCase,
    private val getNavigationTypeUseCase: GetNavigationTypeUseCase,
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
    private val agentMutableStateFlow = MutableStateFlow<AddPropertyAgentViewStateItem?>(null)
    private val pictureMutableStateFlow = MutableStateFlow<List<AddPropertyPictureStateItem>>(emptyList())
    private val isEveryFieldFilledMutableStateFlow = MutableStateFlow(false)
    private val onCreateButtonClicked = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

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
            pictureMutableStateFlow
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
                    agent = AddPropertyAgentViewStateItem(
                        id = agent?.id ?: 0,
                        name = agent?.name ?: "",
                    ),
                    pictures = pictures.map { picture ->
                        AddPropertyPictureStateItem(
                            uri = picture.uri,
                            description = picture.description,
                            isFeatured = picture.isFeatured,
                        )
                    },
                )
            )

            onCreateButtonClicked.collect {
                // Add property
                // TODO: NINO ça va être compliqué pour l'id de property (#PropertyEntity)
                viewModelScope.launch(coroutineDispatcherProvider.io) {
                    val number = addPropertyUseCase.invoke(
                        PropertyEntity(
                            id = 0,
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
                                    id = 0,
                                    type = AmenityType.PARK,
                                    propertyId = 4,
                                ),
                                AmenityEntity(
                                    id = 0,
                                    type = AmenityType.SCHOOL,
                                    propertyId = 4,
                                ),
                                AmenityEntity(
                                    id = 0,
                                    type = AmenityType.GYM,
                                    propertyId = 4,
                                )
                            ),
                            pictures = listOf(
                                PictureEntity(
                                    id = 0,
                                    uri = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dmlsbGF8ZW58MHx8MHx8fDA%3D&w=300&q=300",
                                    description = "Villa",
                                    isThumbnail = true,
                                    propertyId = 4,
                                ),
                                PictureEntity(
                                    id = 0,
                                    uri = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dmlsbGF8ZW58MHx8MHx8fDA%3D&w=300&q=300",
                                    description = "Villa",
                                    isThumbnail = false,
                                    propertyId = 4,
                                ),
                            ),
                            location = LocationEntity(
                                id = 0,
                                propertyId = 4,
                                address = "1 rue de la paix",
                                postalCode = "75000",
                                city = "Paris",
                                latitude = 48.8566,
                                longitude = 2.3522,
                            )
                        )
                    )

                }

            }
        }.collect()
    }


    val viewStateLiveData: LiveData<AddPropertyViewState> = liveData {
        combine(
            getAgentsFlowUseCase.invoke(),
            getPropertyTypeFlowUseCase.invoke(),
        ) { agents, propertyTypes ->
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

    fun onAddressChanged(address: String?) {
        if (address.isNullOrBlank()) {
            addressMutableStateFlow.value = null
        } else {
            addressMutableStateFlow.value = address
        }
    }

    fun onPriceChanged(price: String?) {
        if (price.isNullOrBlank()) {
            priceMutableStateFlow.value = null
        } else {
            priceMutableStateFlow.value = price
        }
    }

    fun onSurfaceChanged(surface: String?) {
        if (surface.isNullOrBlank()) {
            surfaceMutableStateFlow.value = null
        } else {
            surfaceMutableStateFlow.value = surface
        }
    }

    fun onAddPropertyClicked() {
        onCreateButtonClicked.tryEmit(Unit)
    }
}
