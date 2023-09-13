package com.emplk.realestatemanager.domain.pictures

interface PictureRepository {
    suspend fun add(pictureEntity: PictureEntity, propertyId: Long): Boolean
    suspend fun update(pictureEntity: PictureEntity, propertyId: Long): Boolean
}