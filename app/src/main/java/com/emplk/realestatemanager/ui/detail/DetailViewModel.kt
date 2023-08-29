package com.emplk.realestatemanager.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.domain.amenity.Amenity
import com.emplk.realestatemanager.domain.get_properties.GetPropertyByItsIdUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getPropertyByItsIdUseCase: GetPropertyByItsIdUseCase,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    val viewState: LiveData<DetailViewState> = liveData(Dispatchers.IO) {

        val id = savedStateHandle.get<Long>(DetailFragment.EXTRA_ESTATE_ID)
        id?.let { propertyId ->
            getPropertyByItsIdUseCase.invoke(propertyId).collect() {
                emit(
                    DetailViewState(
                        id = it.property.id,
                        type = it.property.type,
                        price = it.property.price.toString(),
                        surface = it.property.surface.toString(),
                        rooms = it.property.rooms.toString(),
                        bathrooms = it.property.bathrooms.toString(),
                        bedrooms = it.property.bedrooms.toString(),
                        description = it.property.description,
                        address = it.location.address,
                        amenitySchool = it.property.amenities.contains(Amenity.SCHOOL),
                        amenityPark = it.property.amenities.contains(Amenity.PARK),
                        amenityShoppingMall = it.property.amenities.contains(Amenity.SHOPPING_MALL),
                        amenityRestaurant = it.property.amenities.contains(Amenity.RESTAURANT),
                        amenityFitnessCenter = it.property.amenities.contains(Amenity.FITNESS_CENTER),
                        amenityPublicTransportation = it.property.amenities.contains(Amenity.PUBLIC_TRANSPORTATION),
                        amenityHospital = it.property.amenities.contains(Amenity.HOSPITAL),
                        amenityLibrary = it.property.amenities.contains(Amenity.LIBRARY),
                        entryDate = it.property.entryDate.toString(),
                        agentName = it.property.agentName,
                        isSold = it.property.isSold,
                        saleDate = it.property.saleDate.toString(),
                    )
                )
            }
        }
    }
}