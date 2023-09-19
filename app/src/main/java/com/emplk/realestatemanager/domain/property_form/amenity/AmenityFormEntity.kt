package com.emplk.realestatemanager.domain.property_form.amenity

data class AmenityFormEntity(
    val id: Long = 0,
    val propertyFormId: Long,
    val type: String,
)
