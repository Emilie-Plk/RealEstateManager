package com.emplk.realestatemanager.domain.pictures

interface PictureRepository {
    suspend fun add(pictureEntity: PictureEntity)
    suspend fun update(pictureEntity: PictureEntity)
}