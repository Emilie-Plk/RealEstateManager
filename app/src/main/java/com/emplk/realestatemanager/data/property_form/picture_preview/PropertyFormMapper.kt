package com.emplk.realestatemanager.data.property_form.picture_preview

import com.emplk.realestatemanager.data.property_form.PropertyFormDto
import com.emplk.realestatemanager.domain.property_form.PropertyFormEntity
import javax.inject.Inject

class PropertyFormMapper @Inject constructor() {

        fun mapToPropertyFormDto(propertyForm: PropertyFormEntity): PropertyFormDto =
            PropertyFormDto(
                id = propertyForm.id,
                type = propertyForm.type,
                price = propertyForm.price,
                surface = propertyForm.surface,
                rooms = propertyForm.rooms,
                bedrooms = propertyForm.bedrooms,
                bathrooms = propertyForm.bathrooms,
                description = propertyForm.description,
                agentName = propertyForm.agentName,
            )

        fun mapToPropertyFormEntity(propertyFormDto: PropertyFormDto): PropertyFormEntity =
            PropertyFormEntity(
                id = propertyFormDto.id,
                type = propertyFormDto.type,
                price = propertyFormDto.price,
                surface = propertyFormDto.surface,
                rooms = propertyFormDto.rooms,
                bedrooms = propertyFormDto.bedrooms,
                bathrooms = propertyFormDto.bathrooms,
                description = propertyFormDto.description,
                agentName = propertyFormDto.agentName,
            )
}