package com.emplk.realestatemanager.data.picture

import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.pictures.PictureEntity
import com.emplk.realestatemanager.domain.pictures.PictureRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PictureRepositoryRoom @Inject constructor(
    private val pictureDao: PictureDao,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PictureRepository {
    override suspend fun addPicture(picture: PictureEntity) {
        withContext(coroutineDispatcherProvider.io) {
            pictureDao.insert(picture)
        }
    }

    override suspend fun updatePicture(picture: PictureEntity) {
        withContext(coroutineDispatcherProvider.io) {
            pictureDao.update(picture)
        }
    }
}