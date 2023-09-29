package com.emplk.realestatemanager.data.property_form.picture_preview

import android.content.Context
import android.database.sqlite.SQLiteException
import android.net.Uri
import android.os.Environment
import androidx.core.content.FileProvider
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.property_form.picture_preview.PicturePreviewEntity
import com.emplk.realestatemanager.domain.property_form.picture_preview.PicturePreviewRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import java.io.File
import java.time.Clock
import javax.inject.Inject

class PicturePreviewRepositoryRoom @Inject constructor(
    private val picturePreviewDao: PicturePreviewDao,
    private val picturePreviewMapper: PicturePreviewMapper,
    @ApplicationContext private val context: Context,
    private val clock: Clock,
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

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun saveToAppFiles(uri: Uri): String? = withContext(coroutineDispatcherProvider.io) {
        try {
            val contentResolver = context.contentResolver

            // inputStream from the selected URI
            val inputStream = contentResolver.openInputStream(uri)

            if (inputStream != null) {
                // File object in your app's private files directory
                val picturesDirectory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "")
                val pictureFileName = "propertyPic_${System.currentTimeMillis()}.jpg"
                val pictureFile = File(picturesDirectory, pictureFileName)

                // Copy the content from the InputStream to the File
                pictureFile.outputStream().use { outputStream ->
                    inputStream.copyTo(outputStream)
                }

                // Close the InputStream
                inputStream.close()

                // Get the URI for the saved image file
                FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    pictureFile
                ).toString()

                return@withContext FileProvider.getUriForFile(
                    context,
                    "${context.packageName}.provider",
                    pictureFile
                ).toString()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return@withContext null
    }


    override fun getAllAsFlow(propertyFormId: Long): Flow<List<PicturePreviewEntity>> = picturePreviewDao
        .getAllAsFlow(propertyFormId)
        .map { picturePreviewDtoList ->
            picturePreviewDtoList.map { picturePreviewDto ->
                picturePreviewMapper.mapToPicturePreviewEntity(picturePreviewDto)
            }
        }
        .flowOn(coroutineDispatcherProvider.io)

    override suspend fun getAll(propertyFormId: Long): List<PicturePreviewEntity> =
        withContext(coroutineDispatcherProvider.io) {
            picturePreviewDao.getAll(propertyFormId).map { picturePreviewDto ->
                picturePreviewMapper.mapToPicturePreviewEntity(picturePreviewDto)
            }
        }

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

    override suspend fun deleteAll(picturePreviewId: Long): Boolean = withContext(coroutineDispatcherProvider.io) {
        picturePreviewDao.deleteAll(picturePreviewId) != null
    }
}