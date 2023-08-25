package com.emplk.realestatemanager.data

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.data.utils.fromJson
import com.emplk.realestatemanager.domain.add_property.AddPropertyUseCase
import com.emplk.realestatemanager.domain.entities.LocationEntity
import com.emplk.realestatemanager.domain.entities.PictureEntity
import com.emplk.realestatemanager.domain.entities.PropertyEntity
import com.emplk.realestatemanager.domain.location.AddLocationUseCase
import com.emplk.realestatemanager.domain.pictures.AddPictureUseCase
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
    private val addPropertyUseCase: AddPropertyUseCase,
    private val addLocationUseCase: AddLocationUseCase,
    private val addPictureUseCase: AddPictureUseCase,
    private val gson: Gson,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_INPUT_DATA_PROPERTIES = "KEY_INPUT_DATA_PROPERTIES"
        const val KEY_INPUT_DATA_LOCATIONS = "KEY_INPUT_DATA_LOCATIONS"
        const val KEY_INPUT_DATA_PICTURES = "KEY_INPUT_DATA_PICTURES"
    }

    override suspend fun doWork(): Result = withContext(coroutineDispatcherProvider.io) {
        val propertiesAsJson = inputData.getString(KEY_INPUT_DATA_PROPERTIES)
        val locationsAsJson = inputData.getString(KEY_INPUT_DATA_LOCATIONS)
        val picturesAsJson = inputData.getString(KEY_INPUT_DATA_PICTURES)
        Log.d("COUCOU", "entitiesAsJson : $propertiesAsJson")

        if (propertiesAsJson != null && locationsAsJson != null && picturesAsJson != null) {
            val propertyEntities = gson.fromJson<List<PropertyEntity>>(json = propertiesAsJson)
            val locationEntities = gson.fromJson<List<LocationEntity>>(json = locationsAsJson)
            val pictureEntities = gson.fromJson<List<PictureEntity>>(json = picturesAsJson)

            if (propertyEntities != null && locationEntities != null && pictureEntities != null) {
                val locationJobs = locationEntities.map { locationEntity ->
                    async { addLocationUseCase.invoke(locationEntity) }
                }

                val pictureJobs = pictureEntities.map { pictureEntity ->
                    async { addPictureUseCase.invoke(pictureEntity) }
                }

                val propertyJobs = propertyEntities.map { propertyEntity ->
                    async { addPropertyUseCase.invoke(propertyEntity) }
                }

                // Wait for all jobs to complete
                val allJobs = locationJobs + pictureJobs + propertyJobs
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