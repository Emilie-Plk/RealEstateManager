package com.emplk.realestatemanager.ui.filter

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FilterPropertiesViewModel @Inject constructor() {

    val viewState: LiveData<FilterViewState> = liveData {

    }
}