package com.emplk.realestatemanager.data.picture

import com.emplk.realestatemanager.domain.pictures.PictureEntity
import javax.inject.Inject

class PictureDtoEntityMapper @Inject constructor() {

    fun mapToDtoEntity(picture: PictureEntity, propertyId: Long): PictureDtoEntity {
        return PictureDtoEntity(
            id = picture.id,
            propertyId = propertyId,
            uri = picture.uri,
            isThumbnail = picture.isThumbnail,
            description = picture.description,
        )
    }

    fun mapToDtoEntities(pictures: List<PictureEntity>, propertyId: Long): List<PictureDtoEntity> {
        return pictures.map {
            PictureDtoEntity(
                id = it.id,
                propertyId = propertyId,
                uri = it.uri,
                isThumbnail = it.isThumbnail,
                description = it.description,
            )
        }
    }

    fun mapToDomainEntity(pictureDtoEntity: PictureDtoEntity): PictureEntity {
        return PictureEntity(
            id = pictureDtoEntity.id,
            propertyId = pictureDtoEntity.propertyId,
            uri = pictureDtoEntity.uri,
            isThumbnail = pictureDtoEntity.isThumbnail,
            description = pictureDtoEntity.description,
        )
    }

    fun mapToDomainEntities(pictureDtoEntities: List<PictureDtoEntity>): List<PictureEntity> {
        return pictureDtoEntities.map {
            PictureEntity(
                id = it.id,
                propertyId = it.propertyId,
                uri = it.uri,
                isThumbnail = it.isThumbnail,
                description = it.description,
            )
        }
    }
}