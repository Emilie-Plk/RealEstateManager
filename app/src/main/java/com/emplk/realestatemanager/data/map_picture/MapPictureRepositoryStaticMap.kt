package com.emplk.realestatemanager.data.map_picture

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Environment
import android.util.LruCache
import androidx.core.content.FileProvider
import com.emplk.realestatemanager.data.api.GoogleApi
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.map_picture.MapPictureRepository
import com.emplk.realestatemanager.domain.map_picture.MapWrapper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class MapPictureRepositoryStaticMap @Inject constructor(
    private val googleApi: GoogleApi,
    @ApplicationContext private val context: Context,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : MapPictureRepository {

    private val mapWrapperLruCache = LruCache<String, MapWrapper>(200)

    override suspend fun getMapPicture(
        latitude: String,
        longitude: String,
        zoom: String,
        size: String,
        markers: String
    ): MapWrapper = withContext(coroutineDispatcherProvider.io) {
        mapWrapperLruCache.get("$latitude,$longitude") ?: try {
            val byteArray = googleApi.getMap(
                center = "$latitude,$longitude",
                zoom = zoom,
                size = size,
                markers = markers
            )
            if (byteArray.isSuccessful) {
                val picturePath = savePicture(byteArray, latitude, longitude)
                if (picturePath != null) {
                    MapWrapper.Success(picturePath)
                } else {
                    MapWrapper.Error
                }
            } else {
                MapWrapper.Error
            }.also { mapWrapperLruCache.put("$latitude,$longitude", it) }
        } catch (e: Exception) {
            e.printStackTrace()
            MapWrapper.Error
        }
    }

    private fun savePicture(byteArray: Response<ResponseBody>, latitude: String, longitude: String): String? {
        try {
            val picturesDirectory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "")
            val pictureFileName = "JPEG_$latitude$longitude.jpg"
            val pictureFile = File(picturesDirectory, pictureFileName)
            val picturePath = FileProvider.getUriForFile(
                context,
                "${context.packageName}.provider",
                pictureFile
            ).toString()

            val outputStream = FileOutputStream(pictureFile)
            BitmapFactory
                .decodeStream(byteArray.body()?.byteStream())
                .compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
            return picturePath
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
