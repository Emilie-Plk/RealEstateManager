package com.emplk.realestatemanager.data.picture

import com.emplk.realestatemanager.domain.pictures.PictureEntity
import javax.inject.Inject

class PictureMapper @Inject constructor() {

    fun mapToDtoEntity(picture: PictureEntity, propertyId: Long): PictureDto {
        return PictureDto(
            id = picture.id,
            propertyId = propertyId,
            uri = picture.uri,
            isFeatured = picture.isFeatured,
            description = picture.description,
        )
    }

    fun mapToDtoEntities(pictures: List<PictureEntity>, propertyId: Long): List<PictureDto> {
        return pictures.map {
            PictureDto(
                id = it.id,
                propertyId = propertyId,
                uri = it.uri,
                isFeatured = it.isFeatured,
                description = it.description,
            )
        }
    }

    fun mapToDomainEntity(pictureDto: PictureDto): PictureEntity {
        return PictureEntity(
            id = pictureDto.id,
            propertyId = pictureDto.propertyId,
            uri = pictureDto.uri,
            isFeatured = pictureDto.isFeatured,
            description = pictureDto.description,
        )
    }

    fun mapToDomainEntities(pictureDtoEntities: List<PictureDto>): List<PictureEntity> {
        return pictureDtoEntities.map {
            PictureEntity(
                id = it.id,
                propertyId = it.propertyId,
                uri = it.uri,
                isFeatured = it.isFeatured,
                description = it.description,
            )
        }
    }
}