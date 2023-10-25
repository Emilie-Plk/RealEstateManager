package com.emplk.realestatemanager.data.property_draft

import android.database.sqlite.SQLiteException
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
    private val formDraftMapper: FormDraftMapper,
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
                    formDraftEntity.pictures.onEach {
                        add(
                            async {
                                val picturePreviewFormDto =
                                    picturePreviewMapper.mapToPicturePreviewDto(it, propertyFormId)
                                picturePreviewDao.insert(picturePreviewFormDto)
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

    override suspend fun doesDraftExist(propertyFormId: Long?): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            if (propertyFormId == null) return@withContext false
            try {
                formDraftDao.doesDraftExist(propertyFormId)
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
            )
        }

    override suspend fun getDraftsCount(): Int = withContext(coroutineDispatcherProvider.io) {
        formDraftDao.getAddPropertyDraftsCount()
    }

    override suspend fun getAllDrafts(): List<FormWithTitleAndLastEditionDate> =
        withContext(coroutineDispatcherProvider.io) {
            formDraftDao.getAllDrafts()
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
                propertyFormDto.amenitySchool,
                propertyFormDto.amenityPark,
                propertyFormDto.amenityMall,
                propertyFormDto.amenityRestaurant,
                propertyFormDto.amenityConcierge,
                propertyFormDto.amenityGym,
                propertyFormDto.amenityTransportation,
                propertyFormDto.amenityHospital,
                propertyFormDto.amenityLibrary,
                propertyFormDto.agentName,
                propertyFormDto.isSold,
                propertyFormDto.saleDate,
                propertyFormDto.lastEditionDate,
                propertyFormDto.id
            )
        }

    override suspend fun updateIsAddressValid(propertyFormId: Long, isAddressValid: Boolean) =
        withContext(coroutineDispatcherProvider.io) {
            formDraftDao.updateIsAddressValid(propertyFormId, isAddressValid)
        }

    override suspend fun delete(propertyFormId: Long): Boolean = withContext(coroutineDispatcherProvider.io) {
        try {
            buildList {
                add(async { picturePreviewDao.deleteAll(propertyFormId) })

                add(async { formDraftDao.delete(propertyFormId) })
            }.awaitAll().all { it != null }
        } catch (e: SQLiteException) {
            e.printStackTrace()
            false
        }
    }
}