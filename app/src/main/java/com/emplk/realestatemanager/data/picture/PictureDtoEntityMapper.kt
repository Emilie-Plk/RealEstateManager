package com.emplk.realestatemanager.data.picture

import com.emplk.realestatemanager.domain.pictures.PictureEntity
import javax.inject.Inject

class PictureDtoEntityMapper @Inject constructor() {

    fun mapToDtoEntities(pictures: List<PictureEntity>): List<PictureDtoEntity> {
        return pictures.map {
            PictureDtoEntity(
                id = it.id,
                propertyId = it.propertyId,
                uri = it.uri,
                isThumbnail = it.isThumbnail,
                description = it.description,
            )
        }
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