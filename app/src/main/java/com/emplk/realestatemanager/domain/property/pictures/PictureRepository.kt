package com.emplk.realestatemanager.domain.property.pictures

interface PictureRepository {
    suspend fun add(pictureEntity: PictureEntity, propertyId: Long): Boolean
    suspend fun update(pictureEntity: PictureEntity, propertyId: Long): Boolean
}