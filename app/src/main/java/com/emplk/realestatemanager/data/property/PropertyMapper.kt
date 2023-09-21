package com.emplk.realestatemanager.data.property

import com.emplk.realestatemanager.data.amenity.AmenityDto
import com.emplk.realestatemanager.data.amenity.AmenityMapper
import com.emplk.realestatemanager.data.location.LocationDto
import com.emplk.realestatemanager.data.location.LocationMapper
import com.emplk.realestatemanager.data.picture.PictureDto
import com.emplk.realestatemanager.data.picture.PictureMapper
import com.emplk.realestatemanager.domain.property.PropertyEntity
import javax.inject.Inject

class PropertyMapper @Inject constructor(
    private val locationMapper: LocationMapper,
    private val pictureMapper: PictureMapper,
    private val amenityMapper: AmenityMapper,
) {

    fun mapToDtoEntity(property: PropertyEntity) = PropertyDto(
        id = property.id,
        type = property.type,
        price = property.price,
        surface = property.surface,
        rooms = property.rooms,
        bedrooms = property.bedrooms,
        bathrooms = property.bathrooms,
        description = property.description,
        agentName = property.agentName,
        isAvailableForSale = property.isAvailableForSale,
        isSold = property.isSold,
        entryDate = property.entryDate,
        saleDate = property.saleDate,
    )


    fun mapToDomainEntity(
        propertyDto: PropertyDto,
        locationDto: LocationDto,
        pictureDtos: List<PictureDto>,
        amenityDtos: List<AmenityDto>,
    ) = PropertyEntity(
        id = propertyDto.id,
        type = propertyDto.type,
        price = propertyDto.price,
        surface = propertyDto.surface,
        location = locationMapper.mapToDomainEntity(locationDto),
        pictures = pictureMapper.mapToDomainEntities(pictureDtos),
        amenities = amenityMapper.mapToDomainEntities(amenityDtos),
        rooms = propertyDto.rooms,
        bedrooms = propertyDto.bedrooms,
        bathrooms = propertyDto.bathrooms,
        description = propertyDto.description,
        agentName = propertyDto.agentName,
        isAvailableForSale = propertyDto.isAvailableForSale,
        isSold = propertyDto.isSold,
        entryDate = propertyDto.entryDate,
        saleDate = propertyDto.saleDate,
    )
}
