package com.emplk.realestatemanager.domain.property.pictures

interface PictureRepository {
    suspend fun add(pictureEntity: PictureEntity, propertyId: Long): Boolean
    suspend fun getPicturesIds(propertyId: Long): List<Long>
    suspend fun getPicturesUris(pictureIds: List<Long>): List<String>
    suspend fun update(pictureEntity: PictureEntity, propertyId: Long): Boolean
    suspend fun upsert(pictureEntity: PictureEntity, propertyId: Long): Long
    suspend fun delete(pictureId: Long)
}