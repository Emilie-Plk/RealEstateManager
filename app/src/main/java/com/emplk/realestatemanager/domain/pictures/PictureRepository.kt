package com.emplk.realestatemanager.domain.pictures

interface PictureRepository {
    suspend fun addPicture(pictureEntity: PictureEntity)
    suspend fun updatePicture(pictureEntity: PictureEntity)
}