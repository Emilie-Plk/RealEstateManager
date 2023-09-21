package com.emplk.realestatemanager.ui.add

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.R
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.agent.GetAgentsFlowUseCase
import com.emplk.realestatemanager.domain.amenity.AmenityType
import com.emplk.realestatemanager.domain.amenity.type.GetAmenityTypeFlowUseCase
import com.emplk.realestatemanager.domain.locale_formatting.CurrencyType
import com.emplk.realestatemanager.domain.locale_formatting.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.locale_formatting.GetSurfaceUnitUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.property.AddPropertyUseCase
import com.emplk.realestatemanager.domain.property_type.GetPropertyTypeFlowUseCase
import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentViewStateItem
import com.emplk.realestatemanager.ui.add.amenity.AmenityViewStateItem
import com.emplk.realestatemanager.ui.add.picture_preview.PicturePreviewStateItem
import com.emplk.realestatemanager.ui.add.type.AddPropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.EquatableCallbackWithParam
import com.emplk.realestatemanager.ui.utils.Event
import com.emplk.realestatemanager.ui.utils.NativeText
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val addPropertyUseCase: AddPropertyUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase,
    private val getSurfaceUnitUseCase: GetSurfaceUnitUseCase,
    private val getAgentsFlowUseCase: GetAgentsFlowUseCase,
    private val getAmenityTypeFlowUseCase: GetAmenityTypeFlowUseCase,
    private val getPropertyTypeFlowUseCase: GetPropertyTypeFlowUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    companion object {
        private const val PROPERTY_FORM_ID = 1L
    }

    private data class AddPropertyForm(
        val propertyType: String? = null,
        val address: String? = null,
        val price: BigDecimal = BigDecimal.ZERO,
        val surface: String? = null,
        val description: String? = null,
        val nbRooms: Int = 0,
        val nbBathrooms: Int = 0,
        val nbBedrooms: Int = 0,
        val agent: String? = null,
        val amenities: List<AmenityViewStateItem> = emptyList(),
        val pictures: List<PicturePreviewStateItem.AddPropertyPicturePreview> = emptyList(),
    )

    private val formMutableStateFlow = MutableStateFlow(AddPropertyForm())

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
        combine(
                formMutableStateFlow,
        getAgentsFlowUseCase.invoke(),
        getPropertyTypeFlowUseCase.invoke(),
        getAmenityTypeFlowUseCase.invoke(),
        isAddingPropertyInDatabaseMutableStateFlow,
        ) { form, agents, propertyTypes, amenityTypes, isAddingPropertyInDatabase ->
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
                selectedAmenities = form.amenities,
                pictures = form.pictures,
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
                amenities = mapAmenityTypesToViewStates(amenityTypes),
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

    private fun mapAmenityTypesToViewStates(amenityTypes: List<AmenityType>): List<AmenityViewStateItem> {
        val viewStates = amenityTypes.map { amenityType ->
            AmenityViewStateItem(
                id = amenityType.id,
                name = amenityType.name,
                isChecked = false,
                iconDrawable = amenityType.iconDrawable,
                stringRes = amenityType.stringRes,
                onCheckBoxClicked = EquatableCallbackWithParam { isChecked ->
                },
            )
        }
        formMutableStateFlow.update {
            it.copy(amenities = it.amenities)
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
        // TODO Add new picture
        formMutableStateFlow.update {
            it.copy(pictures = it.pictures) }
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
