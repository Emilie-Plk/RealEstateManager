package com.emplk.realestatemanager.data

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.emplk.realestatemanager.data.property.PropertyDao
import com.emplk.realestatemanager.data.property.location.LocationDao
import com.emplk.realestatemanager.data.property.picture.PictureDao
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.google.gson.Gson
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext

@HiltWorker
class InitializeDatabaseWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val propertyDao: PropertyDao,
    private val pictureDao: PictureDao,
    private val locationDao: LocationDao,
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
        /*   val propertiesAsJson = inputData.getString(KEY_INPUT_DATA_PROPERTIES)
           val locationsAsJson = inputData.getString(KEY_INPUT_DATA_LOCATIONS)
           val picturesAsJson = inputData.getString(KEY_INPUT_DATA_PICTURES)
           val amenitiesAsJson = inputData.getString(KEY_INPUT_DATA_AMENITIES)

           if (propertiesAsJson != null && locationsAsJson != null && picturesAsJson != null && amenitiesAsJson != null) {
               val propertyEntities = gson.fromJson<List<PropertyDto>>(json = propertiesAsJson)
               val locationEntities = gson.fromJson<List<LocationDto>>(json = locationsAsJson)
               val pictureEntities = gson.fromJson<List<PictureDto>>(json = picturesAsJson)
               val amenityEntities = gson.fromJson<List<AmenityDto>>(json = amenitiesAsJson)

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

                   allJobs.awaitAll()*/
        Result.success()
        /*  } else {
              Log.e("InitDatabaseWorker", "Gson can't parse properties : $propertiesAsJson")
              Result.failure()
          }
      } else {
          Log.e(
              "InitDatabaseWorker",
              "Failed to get data with key $KEY_INPUT_DATA_PROPERTIES from data: $inputData"
          )
          Result.failure()
      }*/
    }
}