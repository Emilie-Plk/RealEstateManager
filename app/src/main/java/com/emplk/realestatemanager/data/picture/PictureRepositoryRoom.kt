package com.emplk.realestatemanager.data.picture

import android.database.sqlite.SQLiteException
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

    override suspend fun add(pictureEntity: PictureEntity, propertyId: Long): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            try {
                val pictureDtoEntity = pictureDtoEntityMapper.mapToDtoEntity(pictureEntity, propertyId)
                pictureDao.insert(pictureDtoEntity)  == 1L
            } catch (e: SQLiteException) {
                e.printStackTrace()
                false
            }
        }

    override suspend fun update(pictureEntity: PictureEntity, propertyId: Long): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            val pictureDtoEntity = pictureDtoEntityMapper.mapToDtoEntity(pictureEntity, propertyId)
            pictureDao.update(pictureDtoEntity) == 1
        }
}