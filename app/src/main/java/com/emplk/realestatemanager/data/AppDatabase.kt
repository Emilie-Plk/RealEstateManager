package com.emplk.realestatemanager.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.workDataOf
import com.emplk.realestatemanager.data.property.PropertyDao
import com.emplk.realestatemanager.data.utils.type_converters.LocalDateTimeTypeConverter
import com.emplk.realestatemanager.data.utils.type_converters.LocationTypeConverter
import com.emplk.realestatemanager.data.utils.type_converters.PointOfInterestListTypeConverter
import com.emplk.realestatemanager.data.utils.type_converters.PropertyPictureListTypeConverter
import com.emplk.realestatemanager.domain.entities.LocationEntity
import com.emplk.realestatemanager.domain.entities.PointOfInterestEntity
import com.emplk.realestatemanager.domain.entities.PropertyEntity
import com.emplk.realestatemanager.domain.entities.PropertyPictureEntity
import com.google.gson.Gson
import java.time.LocalDateTime


@Database(
    entities = [
        PropertyEntity::class,
        PointOfInterestEntity::class,
        PropertyPictureEntity::class,
        LocationEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    LocationTypeConverter::class,
    PropertyPictureListTypeConverter::class,
    PointOfInterestListTypeConverter::class,
    LocalDateTimeTypeConverter::class,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getPropertyDao(): PropertyDao

    companion object {
        private const val DATABASE_NAME = "RealEstateManager_database"

        fun create(
            application: Application,
            workManager: WorkManager,
            gson: Gson,
        ): AppDatabase {
            val builder = Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                DATABASE_NAME
            )

            builder.addCallback(object : Callback() {
                override fun onCreate(db: SupportSQLiteDatabase) {
                    val entitiesAsJson = gson.toJson(
                        listOf(
                            PropertyEntity(
                                id = 0,
                                type = "House",
                                price = 100000,
                                surface = 100,
                                rooms = 5,
                                description = "A beautiful house",
                                photos = listOf(
                                    PropertyPictureEntity(
                                        uri = "https://random.imagecdn.app/300/300",
                                        description = "A beautiful house",
                                        isThumbnail = true
                                    )
                                ),
                                address = "1234 Main St",
                                location = LocationEntity(
                                    latitude = 0.0,
                                    longitude = 0.0
                                ),
                                pointsOfInterest = listOf(
                                    PointOfInterestEntity(
                                        name = "School",
                                        isSelected = true
                                    )
                                ),
                                isAvailableForSale = true,
                                entryDate = LocalDateTime.now(),
                                saleDate = null,
                                agent = "John Doe"
                            ),
                            PropertyEntity(
                                id = 0,
                                type = "Villa",
                                price = 5000000,
                                surface = 200,
                                rooms = 8,
                                description = "A beautiful villa",
                                photos = listOf(
                                    PropertyPictureEntity(
                                        uri = "https://random.imagecdn.app/300/300",
                                        description = "A cool villa",
                                        isThumbnail = true
                                    )
                                ),
                                address = "5678 Main St",
                                location = LocationEntity(
                                    latitude = 0.0,
                                    longitude = 0.0
                                ),
                                pointsOfInterest = listOf(
                                    PointOfInterestEntity(
                                        name = "Hospital",
                                        isSelected = false
                                    )
                                ),
                                isAvailableForSale = true,
                                entryDate = LocalDateTime.now(),
                                saleDate = null,
                                agent = "Jean Doe"
                            ),
                        )
                    )

                    workManager.enqueue(
                        OneTimeWorkRequestBuilder<InitializeDatabaseWorker>()
                            .setInputData(
                                workDataOf(
                                    InitializeDatabaseWorker.KEY_INPUT_DATA to entitiesAsJson
                                )
                            )
                            .build()
                    )
                }
            }
            )
            return builder.build()
        }
    }
}