package com.emplk.realestatemanager.data.picture

import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.pictures.PictureEntity
import com.emplk.realestatemanager.domain.pictures.PictureRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PictureRepositoryRoom @Inject constructor(
    private val pictureDao: PictureDao,
    private val pictureDtoEntityMapper: PictureDtoEntityMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PictureRepository {

    override suspend fun add(pictureEntity: PictureEntity) {
        withContext(coroutineDispatcherProvider.io) {
            val pictureDtoEntity = pictureDtoEntityMapper.mapToDtoEntity(pictureEntity)
            pictureDao.insert(pictureDtoEntity)
        }
    }

    override suspend fun update(pictureEntity: PictureEntity) {
        withContext(coroutineDispatcherProvider.io) {
            val pictureDtoEntity = pictureDtoEntityMapper.mapToDtoEntity(pictureEntity)
            pictureDao.update(pictureDtoEntity)
        }
    }
}