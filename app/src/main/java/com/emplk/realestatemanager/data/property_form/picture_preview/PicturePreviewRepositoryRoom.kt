package com.emplk.realestatemanager.data.property_form.picture_preview

import android.database.sqlite.SQLiteException
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property_form.picture_preview.PicturePreviewEntity
import com.emplk.realestatemanager.domain.property_form.picture_preview.PicturePreviewRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PicturePreviewRepositoryRoom @Inject constructor(
    private val picturePreviewDao: PicturePreviewDao,
    private val picturePreviewMapper: PicturePreviewMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PicturePreviewRepository {
    override suspend fun add(picturePreviewEntity: PicturePreviewEntity, propertyFormId: Long): Long? =
        withContext(coroutineDispatcherProvider.io) {
            try {
                picturePreviewDao.insert(
                    picturePreviewMapper.mapToPicturePreviewDto(picturePreviewEntity, propertyFormId)
                )
            } catch (e: SQLiteException) {
                e.printStackTrace()
                null
            }
        }

    override suspend fun addAll(picturePreviewEntities: List<PicturePreviewEntity>, propertyFormId: Long): List<Long?> =
        withContext(coroutineDispatcherProvider.io) {
            picturePreviewDao.insertAll(
                picturePreviewEntities.map { picturePreviewEntity ->
                    picturePreviewMapper.mapToPicturePreviewDto(picturePreviewEntity, propertyFormId)
                }
            )
        }

    override fun getAllAsFlow(): Flow<List<PicturePreviewEntity>> = picturePreviewDao
        .getAllAsFlow()
        .map { picturePreviewDtoList ->
            picturePreviewDtoList.map { picturePreviewDto ->
                picturePreviewMapper.mapToPicturePreviewEntity(picturePreviewDto)
            }
        }
        .flowOn(coroutineDispatcherProvider.io)

    override suspend fun getPictureById(picturePreviewId: Long): PicturePreviewEntity? =
        withContext(coroutineDispatcherProvider.io) {
            picturePreviewDao.getPictureById(picturePreviewId)?.let { picturePreviewDto ->
                picturePreviewMapper.mapToPicturePreviewEntity(picturePreviewDto)
            }
        }

    override suspend fun update(picturePreviewId: Long, isFeatured: Boolean?, description: String?): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            if (isFeatured != null && isFeatured) {
                picturePreviewDao.clearFeaturedPicture()
            }

            // Set the new picture as featured or update description
            picturePreviewDao.update(
                picturePreviewId,
                isFeatured,
                description
            ) == 1
        }


    override suspend fun delete(picturePreviewId: Long): Boolean = withContext(coroutineDispatcherProvider.io) {
        picturePreviewDao.delete(picturePreviewId) == 1
    }
}