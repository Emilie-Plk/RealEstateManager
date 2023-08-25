package com.emplk.realestatemanager.domain.pictures

interface PictureRepository {
    suspend fun addPicture(picture: PictureEntity)
    suspend fun updatePicture(picture: PictureEntity)
}