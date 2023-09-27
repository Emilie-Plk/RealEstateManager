package com.emplk.realestatemanager.data.property_type

import com.emplk.realestatemanager.domain.property_type.PropertyTypeRepository
import javax.inject.Inject

class PropertyTypeRepositoryImpl @Inject() constructor() : PropertyTypeRepository {
    /*    private val propertyTypeMap: Map<Long, NativeText> = mapOf(
            1L to NativeText.Resource(R.string.property_type_house),
            2L to NativeText.Resource(R.string.property_type_flat),
            3L to NativeText.Resource(R.string.property_type_duplex),
            4L to NativeText.Resource(R.string.property_type_penthouse),
            5L to NativeText.Resource(R.string.property_type_villa),
            6L to NativeText.Resource(R.string.property_type_manor),
            7L to NativeText.Resource(R.string.property_type_other),
        )*/
    private val propertyTypeMap: Map<Long, String> = mapOf(
        1L to "House",
        2L to "Flat",
        3L to "Duplex",
        4L to "Penthouse",
        5L to "Villa",
        6L to "Manor",
        7L to "Other",
    )

    override fun getPropertyTypes(): Map<Long, String> = propertyTypeMap
}