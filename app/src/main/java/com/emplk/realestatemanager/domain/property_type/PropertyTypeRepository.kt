package com.emplk.realestatemanager.domain.property_type

interface PropertyTypeRepository {
    fun getPropertyTypeList(): Map<Long, String>
}
