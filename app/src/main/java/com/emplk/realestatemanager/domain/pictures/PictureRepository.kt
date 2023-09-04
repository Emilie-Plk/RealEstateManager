package com.emplk.realestatemanager.domain.pictures

import com.emplk.realestatemanager.data.picture.PictureDtoEntity

interface PictureRepository {
    suspend fun addPicture(picture: PictureDtoEntity)
    suspend fun updatePicture(picture: PictureDtoEntity)
}