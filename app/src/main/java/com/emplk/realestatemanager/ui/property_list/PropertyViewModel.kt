package com.emplk.realestatemanager.ui.property_list

import androidx.lifecycle.ViewModel
import com.emplk.realestatemanager.domain.get_properties.GetPropertiesAsFlowUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PropertyViewModel @Inject constructor(
private val getPropertiesAsFlowUseCase: GetPropertiesAsFlowUseCase,
) : ViewModel() {
}