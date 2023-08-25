package com.emplk.realestatemanager.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.amenity.Amenity
import com.emplk.realestatemanager.domain.location.AddLocationUseCase
import com.emplk.realestatemanager.domain.location.LocationEntity
import com.emplk.realestatemanager.domain.pictures.AddPictureUseCase
import com.emplk.realestatemanager.domain.pictures.PictureEntity
import com.emplk.realestatemanager.domain.property.AddPropertyUseCase
import com.emplk.realestatemanager.domain.property.PropertyEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val addPropertyUseCase: AddPropertyUseCase,
    private val addLocationUseCase: AddLocationUseCase,
    private val addPictureUseCase: AddPictureUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {
    private val isTabletMutableStateFlow = MutableStateFlow(false)

    fun onAddPropertyClicked() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            val propertyId = addPropertyUseCase.invoke(
                PropertyEntity(
                    id = 0,
                    type = "Flat",
                    price = 100000,
                    surface = 150,
                    rooms = 5,
                    bedrooms = 3,
                    bathrooms = 2,
                    description = "Discover luxury living at its finest with this stunning and spacious home. Boasting elegant design, high-end finishes, and a prime location, this property offers everything you need for a comfortable and lavish lifestyle.",
                    amenities = listOf(
                        Amenity.FITNESS_CENTER
                    ),
                    isAvailableForSale = true,
                    entryDate = LocalDateTime.now(),
                    saleDate = null,
                    isSold = false,
                    agent = "John Doe"
                ),
            )

            addLocationUseCase.invoke(
                LocationEntity(
                    propertyId = propertyId,
                    address = "1234 Main St",
                    city = "Los Angeles",
                    postalCode = "90001",
                    latitude = 34.0522,
                    longitude = -118.2437,
                    neighborhood = "Downtown",
                )
            )

            addPictureUseCase.invoke(
                PictureEntity(
                    propertyId = propertyId,
                    uri = "https://cdn.lecollectionist.com/lc/production/uploads/photos/house-4900/2021-09-15-1b79b0a1369da8839a7c0af22d11690e.jpg?q=65",
                    description = "Living room",
                    isThumbnail = true,
                )
            )
        }
    }

    fun onResume(isTablet: Boolean) {
        isTabletMutableStateFlow.value = isTablet
        Log.d("MainViewModel", "onResume: isTablet = $isTablet")
    }
}