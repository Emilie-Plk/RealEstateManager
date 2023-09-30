package com.emplk.realestatemanager.data.property_form

import android.database.sqlite.SQLiteException
import com.emplk.realestatemanager.data.property_form.amenity.AmenityFormDao
import com.emplk.realestatemanager.data.property_form.amenity.AmenityFormMapper
import com.emplk.realestatemanager.data.property_form.picture_preview.PicturePreviewDao
import com.emplk.realestatemanager.data.property_form.picture_preview.PicturePreviewMapper
import com.emplk.realestatemanager.data.property_form.picture_preview.PropertyFormMapper
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property_form.PropertyFormEntity
import com.emplk.realestatemanager.domain.property_form.PropertyFormRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PropertyFormRepositoryRoom @Inject constructor(
    private val propertyFormDao: PropertyFormDao,
    private val picturePreviewDao: PicturePreviewDao,
    private val amenityFormDao: AmenityFormDao,
    private val propertyFormMapper: PropertyFormMapper,
    private val amenityFormMapper: AmenityFormMapper,
    private val picturePreviewMapper: PicturePreviewMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : PropertyFormRepository {

    private val savePropertyDraftMutableSharedFlow: MutableSharedFlow<Unit> = MutableSharedFlow(extraBufferCapacity = 1)
    private val isPropertyFormInProgressMutableStateFlow = MutableStateFlow(false)

    override suspend fun add(propertyFormEntity: PropertyFormEntity): Long? =
        withContext(coroutineDispatcherProvider.io) {
            try {
                propertyFormDao.insert(propertyFormMapper.mapToPropertyFormDto(propertyFormEntity))
            } catch (e: SQLiteException) {
                e.printStackTrace()
                null
            }
        }

    override suspend fun addPropertyFormWithDetails(propertyFormEntity: PropertyFormEntity): Long =
        withContext(coroutineDispatcherProvider.io) {
            try {
                val propertyFormId = add(propertyFormEntity) ?: return@withContext -1L // TODO: revoir ça et mettre du null

                val picturePreviewsFormAsync = propertyFormEntity.pictures.map { picturePreviewEntity ->
                    async {
                        val picturePreviewFormDto =
                            picturePreviewMapper.mapToPicturePreviewDto(picturePreviewEntity, propertyFormId)
                        picturePreviewDao.insert(picturePreviewFormDto)
                    }
                }

                val amenitiesFormAsync = propertyFormEntity.amenities.map {
                    async {
                        val amenityFormDto = amenityFormMapper.mapToAmenityDto(it, propertyFormId)
                        amenityFormDao.insert(amenityFormDto)
                    }
                }

                (picturePreviewsFormAsync + amenitiesFormAsync).all { it.await() != null }
                propertyFormId
            } catch (e: SQLiteException) {
                e.printStackTrace()
                -1L
            }
        }

    override fun setPropertyFormProgress(isPropertyFormInProgress: Boolean) {
        isPropertyFormInProgressMutableStateFlow.tryEmit(isPropertyFormInProgress)
    }

    override fun isPropertyFormInProgressAsFlow(): Flow<Boolean> =
        isPropertyFormInProgressMutableStateFlow.asStateFlow()

    override suspend fun getExistingPropertyFormId(): Long? = withContext(coroutineDispatcherProvider.io) {
        try {
            propertyFormDao.getExistingPropertyFormId()
        } catch (e: SQLiteException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getExistingPropertyForm(): PropertyFormEntity? = withContext(coroutineDispatcherProvider.io) {
        try {
            propertyFormDao.getExistingPropertyForm()?.let { propertyFormWithDetails ->
                propertyFormMapper.mapToPropertyFormEntity(
                    propertyFormWithDetails.propertyForm,
                    propertyFormWithDetails.picturePreviews,
                    propertyFormWithDetails.amenities,
                )
            }
        } catch (e: SQLiteException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getPropertyFormById(propertyFormId: Long): PropertyFormEntity =
        withContext(coroutineDispatcherProvider.io) {
            propertyFormDao.getPropertyFormById(propertyFormId).let { propertyFormWithDetails ->
                propertyFormMapper.mapToPropertyFormEntity(
                    propertyFormWithDetails.propertyForm,
                    propertyFormWithDetails.picturePreviews,
                    propertyFormWithDetails.amenities,
                )
            }
        }


    override suspend fun exists(): Boolean = withContext(coroutineDispatcherProvider.io) {
        propertyFormDao.exists()
    }

    override suspend fun update(propertyFormEntity: PropertyFormEntity, propertyFormId: Long) =
        withContext(coroutineDispatcherProvider.io) {

            val propertyFormDto = propertyFormMapper.mapToPropertyFormDto(propertyFormEntity)

            propertyFormDao.update(
                propertyFormDto.type,
                propertyFormDto.price,
                propertyFormDto.surface,
                propertyFormDto.address,
                propertyFormDto.rooms,
                propertyFormDto.bedrooms,
                propertyFormDto.bathrooms,
                propertyFormDto.description,
                propertyFormDto.agentName,
                propertyFormId
            )


            val amenityIdsStoredInDb = amenityFormDao.getAllIds(propertyFormId)

            propertyFormEntity.amenities.forEach {
                val amenityFormDto = amenityFormMapper.mapToAmenityDto(it, propertyFormId)
                if (!amenityIdsStoredInDb.contains(amenityFormDto.id)) {
                    amenityFormDao.insert(amenityFormDto)
                }
            }

            amenityIdsStoredInDb.forEach { amenityIdStoredInDatabase ->
                if (propertyFormEntity.amenities.none { it.id == amenityIdStoredInDatabase }) {
                    amenityFormDao.delete(amenityIdStoredInDatabase)
                }
            }
        }


    override suspend fun delete(propertyFormId: Long): Boolean = withContext(coroutineDispatcherProvider.io) {
        val amenityDeletionDeferred = async {
            amenityFormDao.deleteAll(propertyFormId)
        }

        val picturePreviewDeletionDeferred = async {
            picturePreviewDao.deleteAll(propertyFormId)
        }

        val propertyDeletionDeferred = async {
            propertyFormDao.delete(propertyFormId)
        }

        (listOf(propertyDeletionDeferred) + amenityDeletionDeferred + picturePreviewDeletionDeferred).awaitAll()
            .all { it != null }
    }

    override fun onSavePropertyFormEvent() {
        savePropertyDraftMutableSharedFlow.tryEmit(Unit)
    }

    override fun getSavedPropertyFormEvent(): Flow<Unit> = savePropertyDraftMutableSharedFlow
}