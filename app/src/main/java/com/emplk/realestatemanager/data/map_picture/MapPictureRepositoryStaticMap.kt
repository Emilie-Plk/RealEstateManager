package com.emplk.realestatemanager.data.map_picture

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.emplk.realestatemanager.data.api.GoogleApi
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.map_picture.MapPictureRepository
import com.emplk.realestatemanager.domain.map_picture.MapWrapper
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class MapPictureRepositoryStaticMap @Inject constructor(
    private val googleApi: GoogleApi,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : MapPictureRepository {
    override suspend fun getMapPicture(
        latitude: String,
        longitude: String,
        zoom: Int,
        size: String,
        markers: String
    ): MapWrapper = withContext(coroutineDispatcherProvider.io) {
        try {
            val byteArray = googleApi.getMap(
                center = "$latitude,$longitude",
                zoom = zoom,
                size = size,
                markers = markers
            )
            if (byteArray.isSuccessful) {
                val picturePath = savePicture(byteArray, latitude, longitude)
                MapWrapper.Success(picturePath)
            } else {
                MapWrapper.Error
            }
        } catch (e: Exception) {
            e.printStackTrace()
            MapWrapper.Error
        }
    }

    private fun savePicture(byteArray: Response<ResponseBody>, latitude: String, longitude: String): String {
        val picturePath = "/storage/emulated/0/Download/$latitude$longitude.jpg"
        val outputStream = FileOutputStream(File(picturePath))
        BitmapFactory
            .decodeStream(byteArray.body()?.byteStream())
            .compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
        outputStream.close()
        return picturePath
    }
}
