package com.emplk.realestatemanager.data.property_draft

import android.database.sqlite.SQLiteException
import com.emplk.realestatemanager.data.property_draft.amenity.AmenityDraftDao
import com.emplk.realestatemanager.data.property_draft.amenity.AmenityDraftMapper
import com.emplk.realestatemanager.data.property_draft.picture_preview.FormDraftMapper
import com.emplk.realestatemanager.data.property_draft.picture_preview.PicturePreviewDao
import com.emplk.realestatemanager.data.property_draft.picture_preview.PicturePreviewMapper
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property_draft.FormDraftEntity
import com.emplk.realestatemanager.domain.property_draft.FormDraftRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FormDraftDraftRepositoryRoom @Inject constructor(
    private val formDraftDao: FormDraftDao,
    private val picturePreviewDao: PicturePreviewDao,
    private val amenityDraftDao: AmenityDraftDao,
    private val formDraftMapper: FormDraftMapper,
    private val amenityDraftMapper: AmenityDraftMapper,
    private val picturePreviewMapper: PicturePreviewMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : FormDraftRepository {

    override suspend fun add(formDraftEntity: FormDraftEntity): Long =
        withContext(coroutineDispatcherProvider.io) {
            formDraftDao.insert(formDraftMapper.mapToPropertyDraftDto(formDraftEntity))
        }

    override suspend fun addPropertyFormWithDetails(formDraftEntity: FormDraftEntity): Long =
        withContext(coroutineDispatcherProvider.io) {
            add(formDraftEntity).also { propertyFormId ->
                buildList {
                    formDraftEntity.pictures.onEach { picturePreviewEntity ->
                        add(
                            async {
                                val picturePreviewFormDto =
                                    picturePreviewMapper.mapToPicturePreviewDto(picturePreviewEntity, propertyFormId)
                                picturePreviewDao.insert(picturePreviewFormDto)
                            }
                        )
                    }

                    formDraftEntity.amenities.onEach {
                        add(
                            async {
                                val amenityFormDto = amenityDraftMapper.mapToAmenityDto(it, propertyFormId)
                                amenityDraftDao.insert(amenityFormDto)
                            }
                        )
                    }
                }.awaitAll()
            }
        }

    override suspend fun getExistingPropertyFormId(): Long? = withContext(coroutineDispatcherProvider.io) {
        try {
            formDraftDao.getExistingPropertyFormId()
        } catch (e: SQLiteException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun getAddFormId(): Long? = withContext(coroutineDispatcherProvider.io) {
        try {
            formDraftDao.getAddFormId()
        } catch (e: SQLiteException) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun doesPropertyDraftExist(propertyFormId: Long?): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            if (propertyFormId == null) return@withContext false
            try {
                formDraftDao.doesPropertyDraftExist(propertyFormId)
            } catch (e: SQLiteException) {
                e.printStackTrace()
                false
            }
        }

    override suspend fun doesPropertyExist(propertyFormId: Long?): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            if (propertyFormId == null) return@withContext false
            try {
                formDraftDao.doesPropertyExist(propertyFormId)
            } catch (e: SQLiteException) {
                e.printStackTrace()
                false
            }
        }

    override suspend fun getExistingPropertyForm(): FormDraftEntity? = withContext(coroutineDispatcherProvider.io) {
        try {
            formDraftDao.getExistingPropertyForm()?.let { propertyFormWithDetails ->
                formDraftMapper.mapToPropertyDraftEntity(
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

    override suspend fun getPropertyFormById(propertyFormId: Long): FormDraftEntity =
        withContext(coroutineDispatcherProvider.io) {
            val propertyFormWithDetails = formDraftDao.getPropertyFormById(propertyFormId)
            formDraftMapper.mapToPropertyDraftEntity(
                propertyFormWithDetails.propertyForm,
                propertyFormWithDetails.picturePreviews,
                propertyFormWithDetails.amenities,
            )
        }

    override suspend fun update(formDraftEntity: FormDraftEntity) =
        withContext(coroutineDispatcherProvider.io) {

            val propertyFormDto = formDraftMapper.mapToPropertyDraftDto(formDraftEntity)
            formDraftDao.update(
                propertyFormDto.type,
                propertyFormDto.price,
                propertyFormDto.surface,
                propertyFormDto.address,
                propertyFormDto.rooms,
                propertyFormDto.bedrooms,
                propertyFormDto.bathrooms,
                propertyFormDto.description,
                propertyFormDto.agentName,
                propertyFormDto.id
            )


            val amenityIdsStoredInDb = amenityDraftDao.getAllIds(propertyFormDto.id)

            formDraftEntity.amenities.forEach {
                val amenityFormDto = amenityDraftMapper.mapToAmenityDto(it, propertyFormDto.id)
                if (!amenityIdsStoredInDb.contains(amenityFormDto.id)) {
                    amenityDraftDao.insert(amenityFormDto)
                }
            }

            amenityIdsStoredInDb.forEach { amenityIdStoredInDatabase ->
                if (formDraftEntity.amenities.none { it.id == amenityIdStoredInDatabase }) {
                    amenityDraftDao.delete(amenityIdStoredInDatabase)
                }
            }
        }

    override suspend fun updateIsAddressValid(propertyFormId: Long, isAddressValid: Boolean) =
        withContext(coroutineDispatcherProvider.io) {
            formDraftDao.updateIsAddressValid(propertyFormId, isAddressValid)
        }

    override suspend fun delete(propertyFormId: Long): Boolean = withContext(coroutineDispatcherProvider.io) {
        try {
            val amenityDeletionAsync = async { amenityDraftDao.deleteAll(propertyFormId) }

            val picturePreviewDeletionAsync = async { picturePreviewDao.deleteAll(propertyFormId) }

            val propertyDeletionAsync = async { formDraftDao.delete(propertyFormId) }

            (listOf(propertyDeletionAsync) + amenityDeletionAsync + picturePreviewDeletionAsync)
                .all { it.await() != null }
        } catch (e: SQLiteException) {
            e.printStackTrace()
            false
        }
    }
}