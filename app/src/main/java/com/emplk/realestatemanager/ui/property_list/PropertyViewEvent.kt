package com.emplk.realestatemanager.ui.property_list

sealed class PropertyViewEvent {
    object NavigateToDetailActivity : PropertyViewEvent()
    object DisplayDetailFragment : PropertyViewEvent()
}