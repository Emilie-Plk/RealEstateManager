package com.emplk.realestatemanager.ui.detail

import androidx.lifecycle.ViewModel
import com.emplk.realestatemanager.domain.screen_width.SetScreenWidthTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailActivityViewModel @Inject constructor(
    private val setScreenWidthTypeFlowUseCase: SetScreenWidthTypeUseCase,
) : ViewModel() {

    fun onResume(isTablet: Boolean) {
        setScreenWidthTypeFlowUseCase.invoke(isTablet)
    }
}