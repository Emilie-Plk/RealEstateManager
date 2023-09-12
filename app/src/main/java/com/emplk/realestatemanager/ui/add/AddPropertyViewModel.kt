package com.emplk.realestatemanager.ui.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.locale_formatting.GetCurrencyTypeUseCase
import com.emplk.realestatemanager.domain.navigation.GetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.pictures.PictureEntity
import com.emplk.realestatemanager.domain.screen_width.GetScreenWidthTypeFlowUseCase
import com.emplk.realestatemanager.ui.utils.combine
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.collect
import java.math.BigDecimal
import javax.inject.Inject

@HiltViewModel
class AddPropertyViewModel @Inject constructor(
    private val getNavigationTypeUseCase: GetNavigationTypeUseCase,
    private val getScreenWidthTypeFlowUseCase: GetScreenWidthTypeFlowUseCase,
    private val getCurrencyTypeUseCase: GetCurrencyTypeUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    private val propertyTypeMutableStateFlow = MutableStateFlow<String?>(null)
    private val addressMutableStateFlow = MutableStateFlow<String?>(null)
    private val priceMutableStateFlow = MutableStateFlow<BigDecimal?>(null)
    private val surfaceMutableStateFlow = MutableStateFlow(0)
    private val descriptionMutableStateFlow = MutableStateFlow<String?>(null)
    private val nbRoomsMutableStateFlow = MutableStateFlow(0)
    private val nbBathroomsMutableStateFlow = MutableStateFlow(0)
    private val nbBedroomsMutableStateFlow = MutableStateFlow(0)
    private val amenitiesMutableStateFlow = MutableStateFlow<List<String>>(emptyList())
    private val agentNameMutableStateFlow = MutableStateFlow<String?>(null)
    private val pictureMutableStateFlow = MutableStateFlow<List<String>>(emptyList())


    val addPropertyViewStateItem: LiveData<AddPropertyViewStateItem> = liveData {
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
            agentNameMutableStateFlow,
            pictureMutableStateFlow
        ) { propertyType, address, price, surface, description, nbRooms, nbBathrooms, nbBedrooms, amenities, agentName, pictures ->
            emit(
                AddPropertyViewStateItem(
                    propertyType = propertyType ?: "",
                    address = address ?: "",
                    price = price ?: BigDecimal.ZERO,
                    surface = surface,
                    description = description ?: "",
                    nbRooms = nbRooms,
                    nbBathrooms = nbBathrooms,
                    nbBedrooms = nbBedrooms,
                    amenities = amenities,
                    agentName = agentName ?: "",
                    pictureUris = pictures,
                )
            )
        }.collect()
    }

}
