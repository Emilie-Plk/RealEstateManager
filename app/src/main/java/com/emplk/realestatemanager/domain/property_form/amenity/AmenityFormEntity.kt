package com.emplk.realestatemanager.domain.property_form.amenity

data class AmenityFormEntity(
    val id: Long,
    val propertyFormId: Long = 0,
    val type: String? = null,
)
