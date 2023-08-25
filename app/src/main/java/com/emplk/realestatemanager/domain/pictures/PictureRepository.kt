package com.emplk.realestatemanager.domain.pictures

import com.emplk.realestatemanager.domain.entities.PictureEntity

interface PictureRepository {
    suspend fun addPicture(picture: PictureEntity)
    suspend fun updatePicture(picture: PictureEntity)
}