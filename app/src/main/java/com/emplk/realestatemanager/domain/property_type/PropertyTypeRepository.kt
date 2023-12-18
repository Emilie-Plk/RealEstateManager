package com.emplk.realestatemanager.domain.property_type

import com.emplk.realestatemanager.data.property_type.PropertyType

interface PropertyTypeRepository {
    fun getPropertyTypes(): List<PropertyType>
}
