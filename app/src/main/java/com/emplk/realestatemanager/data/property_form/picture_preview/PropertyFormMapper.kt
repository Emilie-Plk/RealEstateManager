package com.emplk.realestatemanager.data.property_form.picture_preview

import com.emplk.realestatemanager.data.property_form.PropertyFormDto
import com.emplk.realestatemanager.data.property_form.amenity.AmenityFormDto
import com.emplk.realestatemanager.data.property_form.amenity.AmenityFormMapper
import com.emplk.realestatemanager.data.property_form.location.LocationFormDto
import com.emplk.realestatemanager.data.property_form.location.LocationFormMapper
import com.emplk.realestatemanager.domain.property_form.PropertyFormEntity
import javax.inject.Inject

class PropertyFormMapper @Inject constructor(
    private val locationFormMapper: LocationFormMapper,
    private val picturePreviewMapper: PicturePreviewMapper,
    private val amenityFormMapper: AmenityFormMapper,
) {

    fun mapToPropertyFormDto(propertyForm: PropertyFormEntity): PropertyFormDto =
        PropertyFormDto(
            type = propertyForm.type,
            price = propertyForm.price,
            surface = propertyForm.surface,
            rooms = propertyForm.rooms,
            bedrooms = propertyForm.bedrooms,
            bathrooms = propertyForm.bathrooms,
            description = propertyForm.description,
            agentName = propertyForm.agentName,
        )

    fun mapToPropertyFormEntity(
        propertyFormDto: PropertyFormDto,
        locationFormDto: LocationFormDto,
        picturePreviewFormDtos: List<PicturePreviewFormDto>,
        amenityFormDtos: List<AmenityFormDto>,
    ): PropertyFormEntity =
        PropertyFormEntity(
            type = propertyFormDto.type,
            price = propertyFormDto.price,
            surface = propertyFormDto.surface,
            rooms = propertyFormDto.rooms,
            bedrooms = propertyFormDto.bedrooms,
            bathrooms = propertyFormDto.bathrooms,
            description = propertyFormDto.description,
            agentName = propertyFormDto.agentName,
            location = locationFormMapper.mapToLocationFormEntity(locationFormDto),
            pictures = picturePreviewMapper.mapToPicturePreviewEntities(picturePreviewFormDtos),
            amenities = amenityFormMapper.mapToAmenityFormEntities(amenityFormDtos),
        )
}