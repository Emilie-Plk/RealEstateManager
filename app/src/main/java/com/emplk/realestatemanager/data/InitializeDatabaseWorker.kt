package com.emplk.realestatemanager.data

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.emplk.realestatemanager.data.amenity.AmenityDao
import com.emplk.realestatemanager.data.amenity.AmenityDtoEntity
import com.emplk.realestatemanager.data.location.LocationDao
import com.emplk.realestatemanager.data.location.LocationDtoEntity
import com.emplk.realestatemanager.data.picture.PictureDao
import com.emplk.realestatemanager.data.picture.PictureDtoEntity
import com.emplk.realestatemanager.data.property.PropertyDao
import com.emplk.realestatemanager.data.property.PropertyDtoEntity
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.data.utils.fromJson
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

@HiltWorker
class InitializeDatabaseWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val propertyDao: PropertyDao,
    private val pictureDao: PictureDao,
    private val locationDao: LocationDao,
    private val amenityDao: AmenityDao,
    private val gson: Gson,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_INPUT_DATA_AMENITIES = "KEY_INPUT_DATA_AMENITIES"
        const val KEY_INPUT_DATA_PROPERTIES = "KEY_INPUT_DATA_PROPERTIES"
        const val KEY_INPUT_DATA_LOCATIONS = "KEY_INPUT_DATA_LOCATIONS"
        const val KEY_INPUT_DATA_PICTURES = "KEY_INPUT_DATA_PICTURES"
    }

    override suspend fun doWork(): Result = withContext(coroutineDispatcherProvider.io) {
        val propertiesAsJson = inputData.getString(KEY_INPUT_DATA_PROPERTIES)
        val locationsAsJson = inputData.getString(KEY_INPUT_DATA_LOCATIONS)
        val picturesAsJson = inputData.getString(KEY_INPUT_DATA_PICTURES)
        val amenitiesAsJson = inputData.getString(KEY_INPUT_DATA_AMENITIES)

        if (propertiesAsJson != null && locationsAsJson != null && picturesAsJson != null && amenitiesAsJson != null) {
            val propertyEntities = gson.fromJson<List<PropertyDtoEntity>>(json = propertiesAsJson)
            val locationEntities = gson.fromJson<List<LocationDtoEntity>>(json = locationsAsJson)
            val pictureEntities = gson.fromJson<List<PictureDtoEntity>>(json = picturesAsJson)
            val amenityEntities = gson.fromJson<List<AmenityDtoEntity>>(json = amenitiesAsJson)

            if (propertyEntities != null && locationEntities != null && pictureEntities != null && amenityEntities != null) {
                val allJobs =
                    propertyEntities.map { propertyDto ->
                        async { propertyDao.insert(propertyDto) }
                    } + pictureEntities.map { pictureDto ->
                        async { pictureDao.insert(pictureDto) }
                    } + locationEntities.map { locationDto ->
                        async { locationDao.insert(locationDto) }
                    } + amenityEntities.map { amenityDto ->
                        async { amenityDao.insert(amenityDto) }
                    }

                allJobs.awaitAll()
                Result.success()
            } else {
                Log.e("COUCOU", "Gson can't parse properties : $propertiesAsJson")
                Result.failure()
            }
        } else {
            Log.e(
                "COUCOU",
                "Failed to get data with key $KEY_INPUT_DATA_PROPERTIES from data: $inputData"
            )
            Result.failure()
        }
    }
}