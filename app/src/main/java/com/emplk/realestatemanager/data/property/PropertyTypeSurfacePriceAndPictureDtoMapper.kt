package com.emplk.realestatemanager.data.property

import com.emplk.realestatemanager.domain.property.amenity.AmenityEntity
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.emplk.realestatemanager.domain.property.type_price_surface.PropertyTypePriceAndSurfaceEntity
import javax.inject.Inject

class PropertyTypeSurfacePriceAndPictureDtoMapper @Inject constructor() {
    fun toEntity(propertyTypeSurfacePriceAndPictureDto: PropertyTypeSurfacePriceAndPictureDto) =
        PropertyTypePriceAndSurfaceEntity(
            id = propertyTypeSurfacePriceAndPictureDto.id,
            type = propertyTypeSurfacePriceAndPictureDto.type,
            price = propertyTypeSurfacePriceAndPictureDto.price,
            surface = propertyTypeSurfacePriceAndPictureDto.surface,
            featuredPictureUri = propertyTypeSurfacePriceAndPictureDto.pictureUri,
            pictureUri = propertyTypeSurfacePriceAndPictureDto.pictureUri,
        /*    amenities = propertyTypeSurfacePriceAndPictureDto.amenities.map {
                AmenityEntity(type = AmenityType.valueOf(it)) },*/
            rooms = propertyTypeSurfacePriceAndPictureDto.rooms,
            bedrooms = propertyTypeSurfacePriceAndPictureDto.bedrooms,
            bathrooms = propertyTypeSurfacePriceAndPictureDto.bathrooms,
            description = propertyTypeSurfacePriceAndPictureDto.description,
        )

    fun toDto(propertyTypePriceAndSurfaceEntity: PropertyTypePriceAndSurfaceEntity) =
        PropertyTypeSurfacePriceAndPictureDto(
            id = propertyTypePriceAndSurfaceEntity.id,
            type = propertyTypePriceAndSurfaceEntity.type,
            price = propertyTypePriceAndSurfaceEntity.price,
            surface = propertyTypePriceAndSurfaceEntity.surface,
            pictureUri = propertyTypePriceAndSurfaceEntity.featuredPictureUri,
       //     amenities = propertyTypePriceAndSurfaceEntity.amenities.map { it.type.name },
            rooms = propertyTypePriceAndSurfaceEntity.rooms,
            bedrooms = propertyTypePriceAndSurfaceEntity.bedrooms,
            bathrooms = propertyTypePriceAndSurfaceEntity.bathrooms,
            description = propertyTypePriceAndSurfaceEntity.description,
        )
}