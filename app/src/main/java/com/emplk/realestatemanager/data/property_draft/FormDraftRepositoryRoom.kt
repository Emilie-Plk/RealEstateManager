package com.emplk.realestatemanager.data.property_draft

import android.database.sqlite.SQLiteException
import com.emplk.realestatemanager.data.property_draft.mappers.FormDraftMapper
import com.emplk.realestatemanager.data.property_draft.mappers.PicturePreviewMapper
import com.emplk.realestatemanager.data.property_draft.picture_preview.PicturePreviewDao
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property_draft.FormDraftEntity
import com.emplk.realestatemanager.domain.property_draft.FormDraftRepository
import com.emplk.realestatemanager.domain.property_draft.FormWithDetailEntity
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext
import javax.inject.Inject

class FormDraftRepositoryRoom @Inject constructor(
    private val formDraftDao: FormDraftDao,
    private val picturePreviewDao: PicturePreviewDao,
    private val formDraftMapper: FormDraftMapper,
    private val picturePreviewMapper: PicturePreviewMapper,
    private val formWithTitleDateAndFeaturedPictureMapper: FormWithTitleDateAndFeaturedPictureMapper,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : FormDraftRepository {

    override suspend fun add(formDraftEntity: FormDraftEntity): Long =
        withContext(coroutineDispatcherProvider.io) {
            formDraftDao.insert(formDraftMapper.mapToFormDraftDto(formDraftEntity))
        }

    override suspend fun addFormDraftWithDetails(formDraftEntity: FormDraftEntity): Long =
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


    override suspend fun doesDraftExist(formId: Long?): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            if (formId == null) return@withContext false
            try {
                formDraftDao.doesDraftExist(formId)
            } catch (e: SQLiteException) {
                e.printStackTrace()
                false
            }
        }

    override suspend fun doesPropertyExist(formId: Long?): Boolean =
        withContext(coroutineDispatcherProvider.io) {
            if (formId == null) return@withContext false
            try {
                formDraftDao.doesPropertyExist(formId)
            } catch (e: SQLiteException) {
                e.printStackTrace()
                false
            }
        }

    override suspend fun getFormDraftEntityById(formId: Long): FormDraftEntity =
        withContext(coroutineDispatcherProvider.io) {
            val propertyFormWithDetails = formDraftDao.getPropertyFormById(formId)
            formDraftMapper.mapToFormDraftEntity(
                propertyFormWithDetails.propertyForm,
                propertyFormWithDetails.picturePreviews,
            )
        }

    override suspend fun getDraftsCount(): Int = withContext(coroutineDispatcherProvider.io) {
        formDraftDao.getAddPropertyDraftsCount()
    }

    override suspend fun getFormsWithDetails(): List<FormWithDetailEntity> =
        withContext(coroutineDispatcherProvider.io) {
            val formWithTitleDateAndFeaturedPictures = formDraftDao.getFormsWithFeaturedPicture()
            formWithTitleDateAndFeaturedPictureMapper.mapToFormsWithTitleDateAndFeaturedPictureEntities(
                formWithTitleDateAndFeaturedPictures
            )
        }

    override suspend fun update(formDraftEntity: FormDraftEntity) =
        withContext(coroutineDispatcherProvider.io) {
            val propertyFormDto = formDraftMapper.mapToFormDraftDto(formDraftEntity)
            formDraftDao.update(
                propertyFormDto.type,
                propertyFormDto.title,
                propertyFormDto.price,
                propertyFormDto.surface,
                propertyFormDto.address,
                propertyFormDto.rooms,
                propertyFormDto.bedrooms,
                propertyFormDto.bathrooms,
                propertyFormDto.description,
                propertyFormDto.amenitySchool,
                propertyFormDto.amenityPark,
                propertyFormDto.amenityShopping,
                propertyFormDto.amenityRestaurant,
                propertyFormDto.amenityConcierge,
                propertyFormDto.amenityGym,
                propertyFormDto.amenityTransportation,
                propertyFormDto.amenityHospital,
                propertyFormDto.amenityLibrary,
                propertyFormDto.agentName,
                propertyFormDto.entryDate,
                propertyFormDto.saleDate,
                propertyFormDto.lastEditionDate,
                propertyFormDto.id
            )
        }

    override suspend fun updateAddressValidity(formId: Long, isAddressValid: Boolean) =
        withContext(coroutineDispatcherProvider.io) {
            formDraftDao.updateAddressValidity(formId, isAddressValid)
        }

    override suspend fun delete(formId: Long): Boolean = withContext(coroutineDispatcherProvider.io) {
        try {
            buildList {
                add(async { picturePreviewDao.deleteAll(formId) })

                add(async { formDraftDao.delete(formId) })
            }.awaitAll().all { it != null }
        } catch (e: SQLiteException) {
            e.printStackTrace()
            false
        }
    }
}