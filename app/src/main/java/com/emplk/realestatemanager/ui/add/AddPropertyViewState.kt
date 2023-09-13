package com.emplk.realestatemanager.ui.add

import com.emplk.realestatemanager.ui.add.agent.AddPropertyAgentViewStateItem
import com.emplk.realestatemanager.ui.add.type.AddPropertyTypeViewStateItem
import com.emplk.realestatemanager.ui.utils.NativeText

data class AddPropertyViewState(
    val priceCurrency : NativeText,
    val surfaceUnit : NativeText,
    val isAddButtonEnabled: Boolean,
    val isProgressBarVisible: Boolean,
    val propertyTypes: List<AddPropertyTypeViewStateItem>,
    val agents: List<AddPropertyAgentViewStateItem>,
)