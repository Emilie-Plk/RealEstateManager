package com.emplk.realestatemanager.domain.property_form.location

data class LocationFormEntity(
    val address: String? = null,
    val city: String? = null,
    val postalCode: String? = null,
    val latitude: Double? = null,
    val longitude: Double? = null,
)