package com.emplk.realestatemanager.ui.property_list

sealed class PropertiesViewEvent {
    object NavigateToDetailActivity : PropertiesViewEvent()
    object DisplayDetailFragment : PropertiesViewEvent()
}