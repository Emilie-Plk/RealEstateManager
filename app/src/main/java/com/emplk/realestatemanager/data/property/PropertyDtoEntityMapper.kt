package com.emplk.realestatemanager.data.property

import com.emplk.realestatemanager.data.amenity.AmenityDtoEntity
import com.emplk.realestatemanager.data.amenity.AmenityDtoEntityMapper
import com.emplk.realestatemanager.data.location.LocationDtoEntity
import com.emplk.realestatemanager.data.location.LocationDtoEntityMapper
import com.emplk.realestatemanager.data.picture.PictureDtoEntity
import com.emplk.realestatemanager.data.picture.PictureDtoEntityMapper
import com.emplk.realestatemanager.domain.property.PropertyEntity
import javax.inject.Inject

class PropertyDtoEntityMapper @Inject constructor(
    private val locationDtoEntityMapper: LocationDtoEntityMapper,
    private val pictureDtoEntityMapper: PictureDtoEntityMapper,
    private val amenityDtoEntityMapper: AmenityDtoEntityMapper,
) {

    fun mapToDtoEntity(property: PropertyEntity): PropertyDtoEntity =
        PropertyDtoEntity(
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
        propertyDtoEntity: PropertyDtoEntity,
        locationDtoEntity: LocationDtoEntity,
        pictureDtoEntities: List<PictureDtoEntity>,
        amenityDtoEntities: List<AmenityDtoEntity>,
    ): PropertyEntity =
        PropertyEntity(
            id = propertyDtoEntity.id,
            type = propertyDtoEntity.type,
            price = propertyDtoEntity.price,
            surface = propertyDtoEntity.surface,
            rooms = propertyDtoEntity.rooms,
            bedrooms = propertyDtoEntity.bedrooms,
            bathrooms = propertyDtoEntity.bathrooms,
            description = propertyDtoEntity.description,
            agentName = propertyDtoEntity.agentName,
            isAvailableForSale = propertyDtoEntity.isAvailableForSale,
            isSold = propertyDtoEntity.isSold,
            entryDate = propertyDtoEntity.entryDate,
            saleDate = propertyDtoEntity.saleDate,
            location = locationDtoEntityMapper.mapToDomainEntity(locationDtoEntity),
            pictures = pictureDtoEntityMapper.mapToDomainEntities(pictureDtoEntities),
            amenities = amenityDtoEntityMapper.mapToDomainEntity(amenityDtoEntities),
        )
}
