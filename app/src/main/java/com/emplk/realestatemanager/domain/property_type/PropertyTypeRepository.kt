package com.emplk.realestatemanager.domain.property_type

interface PropertyTypeRepository {
    fun getPropertyTypes(): Map<Long, String>
}
