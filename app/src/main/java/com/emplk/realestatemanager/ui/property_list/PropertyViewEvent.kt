package com.emplk.realestatemanager.ui.property_list

sealed class PropertyViewEvent {
    data class NavigateToDetailActivity(val id: Long) : PropertyViewEvent()
}