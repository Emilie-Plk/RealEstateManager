package com.emplk.realestatemanager.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.screen_width.SetScreenWidthTypeUseCase
import com.emplk.realestatemanager.ui.utils.Event
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val setScreenWidthTypeUseCase: SetScreenWidthTypeUseCase,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {
    private val isTabletMutableStateFlow = MutableStateFlow(false)
    private val hasAddPropertyButtonBeenClickedMutableSharedFlow = MutableSharedFlow<Boolean>()

    val mainViewEventLiveData: LiveData<Event<MainViewEvent>> = liveData {
        combine(
            isTabletMutableStateFlow,
            hasAddPropertyButtonBeenClickedMutableSharedFlow.asSharedFlow(),
        )
        { isTablet, hasButtonBeenClicked ->
            Log.d(
                "COUCOU",
                "isTablet: $isTablet hasButtonBeenClicked: $hasButtonBeenClicked"
            )
            if (hasButtonBeenClicked) {
                if (!isTablet) {
                    emit(
                        Event(
                            MainViewEvent.NavigateToAddPropertyActivity
                        )
                    )
                } else {
                    emit(
                        Event(
                            MainViewEvent.DisplayAddPropertyFragment
                        )
                    )
                }
            } else {
                if (!isTablet) {
                    emit(
                        Event(
                            MainViewEvent.DidNotClickedAddPropertyButtonPhone
                        )
                    )
                } else {
                    emit(
                        Event(
                            MainViewEvent.DidNotClickedAddPropertyButtonTablet
                        )
                    )
                }
            }
        }.collect()
    }

    fun onAddPropertyClicked() {
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            hasAddPropertyButtonBeenClickedMutableSharedFlow.emit(true)
        }
    }

    fun onResume(isTablet: Boolean) {
        isTabletMutableStateFlow.value = isTablet
        setScreenWidthTypeUseCase.invoke(isTablet)
        viewModelScope.launch(coroutineDispatcherProvider.io) {
            hasAddPropertyButtonBeenClickedMutableSharedFlow.emit(false)
        }
        Log.d("COUCOU", "onResume: isTablet = $isTablet")
    }
}