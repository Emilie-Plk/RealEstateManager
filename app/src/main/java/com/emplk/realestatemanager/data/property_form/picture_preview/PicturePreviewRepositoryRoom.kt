package com.emplk.realestatemanager.data.property_form.picture_preview

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
            picturePreviewDao.insert(
                picturePreviewMapper.mapToPicturePreviewDto(picturePreviewEntity, propertyFormId)
            )
        }

    override suspend fun addAll(picturePreviewEntities: List<PicturePreviewEntity>, propertyFormId: Long): List<Long?> =
        withContext(coroutineDispatcherProvider.io) {
            picturePreviewDao.insertAll(
                picturePreviewEntities.map { picturePreviewEntity ->
                    picturePreviewMapper.mapToPicturePreviewDto(picturePreviewEntity, propertyFormId)
                }
            )
        }

    override fun getAsFlow(): Flow<List<PicturePreviewEntity>> = picturePreviewDao
        .getAllAsFlow()
        .map { picturePreviewDtoList ->
            picturePreviewDtoList.map { picturePreviewDto ->
                picturePreviewMapper.mapToPicturePreviewEntity(picturePreviewDto)
            }
        }
        .flowOn(coroutineDispatcherProvider.io)

    override suspend fun update(picturePreviewEntity: PicturePreviewEntity, propertyFormId: Long): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            val picturePreviewFormDto =
                picturePreviewMapper.mapToPicturePreviewDto(picturePreviewEntity, propertyFormId)
            picturePreviewDao.update(picturePreviewFormDto) == 1
        }

    override suspend fun delete(picturePreviewId: Long): Boolean = withContext(coroutineDispatcherProvider.io) {
        picturePreviewDao.delete(picturePreviewId) == 1
    }
}