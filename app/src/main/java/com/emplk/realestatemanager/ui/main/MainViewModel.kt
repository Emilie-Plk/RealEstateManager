package com.emplk.realestatemanager.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.add_property.AddPropertyUseCase
import com.emplk.realestatemanager.domain.entities.Amenity
import com.emplk.realestatemanager.domain.entities.PropertyEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val addPropertyUseCase: AddPropertyUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    private val isTabletMutableStateFlow = MutableStateFlow(false)

    fun onAddPropertyClicked() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            addPropertyUseCase.invoke(
                PropertyEntity(
                    id = 0,
                    type = "Flat",
                    price = 100000,
                    surface = 150,
                    rooms = 5,
                    description = "Discover luxury living at its finest with this stunning and spacious home. Boasting elegant design, high-end finishes, and a prime location, this property offers everything you need for a comfortable and lavish lifestyle.",
                    amenities = listOf(
                        Amenity.FITNESS_CENTER
                    ),
                    isAvailableForSale = true,
                    entryDate = LocalDateTime.of(2023, 8, 24, 10, 0),
                    saleDate = null,
                    isSold = false,
                    agent = "John Doe"
                ),
            )
        }
    }

    fun onResume(isTablet: Boolean) {
        isTabletMutableStateFlow.value = isTablet
        Log.d("MainViewModel", "onResume: isTablet = $isTablet")
    }

}