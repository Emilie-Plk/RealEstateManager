package com.emplk.realestatemanager.domain.property.pictures

interface PictureRepository {
    suspend fun getPicturesIds(propertyId: Long): List<Long>
    suspend fun delete(pictureId: Long)
}