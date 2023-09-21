package com.emplk.realestatemanager.domain.property_form.location

data class LocationFormEntity(
    val id: Long? = 0,
    val propertyFormId: Long?,
    val address: String?,
    val city: String?,
    val postalCode: String?,
    val latitude: Double?,
    val longitude: Double?,
)