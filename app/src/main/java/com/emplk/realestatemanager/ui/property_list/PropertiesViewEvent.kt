package com.emplk.realestatemanager.ui.property_list

sealed class PropertiesViewEvent {
    object DisplayDetailFragmentOnPhone : PropertiesViewEvent()
    object DisplayDetailFragmentOnTablet : PropertiesViewEvent()
}