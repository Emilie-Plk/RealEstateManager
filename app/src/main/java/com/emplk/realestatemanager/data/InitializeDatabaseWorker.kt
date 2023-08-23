package com.emplk.realestatemanager.data

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.emplk.realestatemanager.data.utils.fromJson
import com.emplk.realestatemanager.data.utils.CoroutineDispatcherProvider
import com.emplk.realestatemanager.domain.add_property.AddPropertyUseCase
import com.emplk.realestatemanager.domain.add_property.entities.PropertyEntity
import com.google.gson.Gson

import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.withContext

@HiltWorker
class InitializeDatabaseWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val addPropertyUseCase: AddPropertyUseCase,
    private val gson: Gson,
    private val coroutineDispatcherProvider: CoroutineDispatcherProvider,
) : CoroutineWorker(context, workerParams) {

    companion object {
        const val KEY_INPUT_DATA = "KEY_INPUT_DATA"
    }

    override suspend fun doWork(): Result = withContext(coroutineDispatcherProvider.io) {
        val entitiesAsJson = inputData.getString(KEY_INPUT_DATA)
        Log.d("COUCOU", "entitiesAsJson : $entitiesAsJson")

        if (entitiesAsJson != null) {
            val propertyEntities = gson.fromJson<List<PropertyEntity>>(json = entitiesAsJson)

            if (propertyEntities != null) {
                propertyEntities.forEach { propertyEntity ->
                    addPropertyUseCase.invoke(propertyEntity)
                }
                Result.success()
            } else {
                Log.e("COUCOU", "Gson can't parse properties : $entitiesAsJson")
                Result.failure()
            }
        } else {
            Log.e(
                "COUCOU",
                "Failed to get data with key $KEY_INPUT_DATA from data: $inputData"
            )
            Result.failure()
        }
    }
}