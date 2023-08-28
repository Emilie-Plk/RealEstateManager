package com.emplk.realestatemanager.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.location.AddLocationUseCase
import com.emplk.realestatemanager.domain.pictures.AddPictureUseCase
import com.emplk.realestatemanager.domain.property.AddPropertyUseCase
import com.emplk.realestatemanager.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val addPropertyUseCase: AddPropertyUseCase,
    private val addLocationUseCase: AddLocationUseCase,
    private val addPictureUseCase: AddPictureUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {
    private val isTabletMutableStateFlow = MutableStateFlow(false)
    private val hasAddPropertyButtonBeenClickedMutableSharedFlow = MutableSharedFlow<Boolean>()

    val mainViewActionLiveData: LiveData<Event<MainViewEvent>> = liveData {
        coroutineScope {
            launch {
                combine(hasAddPropertyButtonBeenClickedMutableSharedFlow, isTabletMutableStateFlow)
                { hasAddPropertyButtonBeenClicked, isTablet ->
                    Log.d("COUCOU", "hasAddPropertyButtonBeenClicked: $hasAddPropertyButtonBeenClicked - isTablet: $isTablet")
                    if (hasAddPropertyButtonBeenClicked) {
                        if (!isTablet) {
                            emit(
                                Event(
                                    MainViewEvent.NavigateToDetailActivity
                                )
                            )
                        } else {
                            emit(
                                Event(
                                    MainViewEvent.DoNothingForTheMoment
                                )
                            )
                        }
                    }
                }.asLiveData()
            }
        }
    }

    fun onAddPropertyClicked() {
        hasAddPropertyButtonBeenClickedMutableSharedFlow.tryEmit(true)
    }

    fun onResume(isTablet: Boolean) {
        isTabletMutableStateFlow.value = isTablet
        Log.d("COUCOU", "onResume: isTablet = $isTablet")
    }
}