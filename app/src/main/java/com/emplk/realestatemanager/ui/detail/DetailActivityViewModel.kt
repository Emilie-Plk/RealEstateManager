package com.emplk.realestatemanager.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.navigation.GetToolbarSubtitleUseCase
import com.emplk.realestatemanager.domain.navigation.NavigationFragmentType
import com.emplk.realestatemanager.domain.navigation.SetNavigationTypeUseCase
import com.emplk.realestatemanager.domain.screen_width.GetScreenWidthTypeFlowUseCase
import com.emplk.realestatemanager.domain.screen_width.ScreenWidthType
import com.emplk.realestatemanager.domain.screen_width.SetScreenWidthTypeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailActivityViewModel @Inject constructor(
    private val setScreenWidthTypeFlowUseCase: SetScreenWidthTypeUseCase,
    private val setNavigationTypeUseCase: SetNavigationTypeUseCase,
    private val getScreenWidthTypeFlowUseCase: GetScreenWidthTypeFlowUseCase,
    getToolbarSubtitleUseCase: GetToolbarSubtitleUseCase,
    coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : ViewModel() {

    val isTabletLiveData: LiveData<Boolean> = liveData(coroutineDispatcherProvider.io) {
        getScreenWidthTypeFlowUseCase.invoke().collect { screenWidthType ->
            emit(screenWidthType == ScreenWidthType.TABLET)
        }
    }

    val toolbarSubtitleLiveData: LiveData<String?> = getToolbarSubtitleUseCase.invoke().asLiveData()

    fun onResume(isTablet: Boolean) {
        setScreenWidthTypeFlowUseCase.invoke(isTablet)
    }

    fun onBackPressed() {
        setNavigationTypeUseCase.invoke(NavigationFragmentType.LIST_FRAGMENT)
    }
}