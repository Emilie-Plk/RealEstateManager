package com.emplk.realestatemanager.data.property_form.location

import com.emplk.realestatemanager.domain.property_form.location.LocationFormEntity
import javax.inject.Inject

class LocationFormMapper @Inject constructor() {
    fun mapToLocationFormEntity(locationFormDto: LocationFormDto): LocationFormEntity = LocationFormEntity(
        latitude = locationFormDto.latitude,
        longitude = locationFormDto.longitude,
        address = locationFormDto.address,
        city = locationFormDto.city,
        postalCode = locationFormDto.postalCode,
    )

    fun mapToLocationDto(locationFormEntity: LocationFormEntity?, propertyFormId: Long): LocationFormDto =
        LocationFormDto(
            propertyFormId = propertyFormId,
            latitude = locationFormEntity?.latitude,
            longitude = locationFormEntity?.longitude,
            address = locationFormEntity?.address,
            city = locationFormEntity?.city,
            postalCode = locationFormEntity?.postalCode,
        )

}
