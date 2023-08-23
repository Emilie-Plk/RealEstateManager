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
import com.emplk.realestatemanager.domain.add_property.entities.LocationEntity
import com.emplk.realestatemanager.domain.add_property.entities.PointOfInterestEntity
import com.emplk.realestatemanager.domain.add_property.entities.PropertyEntity
import com.emplk.realestatemanager.domain.add_property.entities.PropertyPictureEntity
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
                                        uri = "https://cf.bstatic.com/xdata/images/hotel/max1024x768/295090917.jpg?k=d17621b71b0eaa0c7a37d8d8d02d33896cef75145f61e7d96d296d88375a7d39&o=&hp=1",
                                        description = "A beautiful house",
                                        isThumbnail = false
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
                                        isSelected = false
                                    )
                                ),
                                isAvailableForSale = true,
                                entryDate = LocalDateTime.now(),
                                saleDate = null,
                                agent = "John Doe"
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