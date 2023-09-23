package com.emplk.realestatemanager.domain.property_form.location

data class LocationFormEntity(
    val id: Long = 0,
    val propertyFormId: Long = 0,
    val address: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)