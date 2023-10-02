package com.emplk.realestatemanager.data.property

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
        )

    fun toDto(propertyTypePriceAndSurfaceEntity: PropertyTypePriceAndSurfaceEntity) =
        PropertyTypeSurfacePriceAndPictureDto(
            id = propertyTypePriceAndSurfaceEntity.id,
            type = propertyTypePriceAndSurfaceEntity.type,
            price = propertyTypePriceAndSurfaceEntity.price,
            surface = propertyTypePriceAndSurfaceEntity.surface,
            pictureUri  = propertyTypePriceAndSurfaceEntity.featuredPictureUri,
        )
}