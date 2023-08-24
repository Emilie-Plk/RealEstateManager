package com.emplk.realestatemanager.ui.main

import android.util.Log
import androidx.lifecycle.ViewModel
import com.emplk.realestatemanager.domain.get_properties.GetPropertiesAsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
) : ViewModel() {

    private val isTabletMutableStateFlow = MutableStateFlow(false)


    fun onResume(isTablet: Boolean) {
        isTabletMutableStateFlow.value = isTablet
        Log.d("MainViewModel", "onResume: isTablet = $isTablet")
    }
}