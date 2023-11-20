package com.emplk.realestatemanager.data.property.picture

import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property.pictures.PictureRepository
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PictureRepositoryRoom @Inject constructor(
    private val pictureDao: PictureDao,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PictureRepository {

    override suspend fun getPicturesIds(propertyId: Long): List<Long> = withContext(coroutineDispatcherProvider.io) {
        pictureDao.getAllPicturesIdsFromProperty(propertyId)
    }

    override suspend fun delete(pictureId: Long) =
        withContext(coroutineDispatcherProvider.io) {
            pictureDao.delete(pictureId)
        }
}