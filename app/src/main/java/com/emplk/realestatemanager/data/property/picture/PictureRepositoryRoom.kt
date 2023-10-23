package com.emplk.realestatemanager.data.property.picture

import android.database.sqlite.SQLiteException
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property.pictures.PictureEntity
import com.emplk.realestatemanager.domain.property.pictures.PictureRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PictureRepositoryRoom @Inject constructor(
    private val pictureDao: PictureDao,
    private val pictureMapper: PictureMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PictureRepository {

    override suspend fun add(pictureEntity: PictureEntity, propertyId: Long): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            try {
                val pictureDtoEntity = pictureMapper.mapToDtoEntity(pictureEntity, propertyId)
                pictureDao.insert(pictureDtoEntity) == 1L
            } catch (e: SQLiteException) {
                e.printStackTrace()
                false
            }
        }

    override suspend fun getPicturesIds(propertyId: Long): List<Long> = withContext(coroutineDispatcherProvider.io) {
        pictureDao.getAllPicturesIds(propertyId)
    }

    override suspend fun getPicturesUris(pictureIds: List<Long>): List<String> = withContext(coroutineDispatcherProvider.io) {
        pictureDao.getPictureUris(pictureIds)
    }

    override suspend fun update(pictureEntity: PictureEntity, propertyId: Long): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            val pictureDtoEntity = pictureMapper.mapToDtoEntity(pictureEntity, propertyId)
            pictureDao.update(pictureDtoEntity) == 1
        }

    override suspend fun upsert(pictureEntity: PictureEntity, propertyId: Long): Long =
        withContext(coroutineDispatcherProvider.io) {
            val pictureDtoEntity = pictureMapper.mapToDtoEntity(pictureEntity, propertyId)
            pictureDao.upsert(pictureDtoEntity)
        }

    override suspend fun delete(pictureId: Long) =
        withContext(coroutineDispatcherProvider.io) {
            pictureDao.delete(pictureId)
        }
}