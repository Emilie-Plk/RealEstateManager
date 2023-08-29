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
import com.emplk.realestatemanager.data.location.LocationDao
import com.emplk.realestatemanager.data.picture.PictureDao
import com.emplk.realestatemanager.data.property.PropertyDao
import com.emplk.realestatemanager.data.utils.type_converters.AmenityTypeConverter
import com.emplk.realestatemanager.data.utils.type_converters.LocalDateTimeTypeConverter
import com.emplk.realestatemanager.domain.amenity.Amenity
import com.emplk.realestatemanager.domain.location.LocationEntity
import com.emplk.realestatemanager.domain.pictures.PictureEntity
import com.emplk.realestatemanager.domain.property.PropertyEntity
import com.google.gson.Gson
import java.time.LocalDateTime


@Database(
    entities = [
        PropertyEntity::class,
        PictureEntity::class,
        LocationEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    LocalDateTimeTypeConverter::class,
    AmenityTypeConverter::class,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getPropertyDao(): PropertyDao
    abstract fun getLocationDao(): LocationDao
    abstract fun getPictureDao(): PictureDao

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
                    val propertiesAsJson = gson.toJson(
                        listOf(
                            PropertyEntity(
                                id = 0,
                                type = "Flat",
                                price = 100000,
                                surface = 150,
                                rooms = 5,
                                bathrooms = 2,
                                bedrooms = 3,
                                description = "Discover luxury living at its finest with this stunning and spacious home. Boasting elegant design, high-end finishes, and a prime location, this property offers everything you need for a comfortable and lavish lifestyle.",
                                amenities = listOf(
                                    Amenity.LIBRARY,
                                    Amenity.PARK,
                                    Amenity.PUBLIC_TRANSPORTATION,
                                    Amenity.FITNESS_CENTER,
                                    Amenity.SCHOOL,
                                ),
                                isAvailableForSale = true,
                                entryDate = LocalDateTime.of(2023, 8, 24, 10, 0),
                                saleDate = null,
                                isSold = false,
                                agentName = "John Doe"
                            ),
                            PropertyEntity(
                                id = 0,
                                type = "Villa",
                                price = 5000000,
                                surface = 200,
                                rooms = 8,
                                bathrooms = 4,
                                bedrooms = 4,
                                description = " Experience the epitome of modern luxury in this exquisite villa that seamlessly blends sophistication with comfort. This architectural masterpiece features sleek lines, floor-to-ceiling windows, and cutting-edge design elements that create an unparalleled living experience. Enjoy spacious living areas, a state-of-the-art kitchen, and breathtaking panoramic views of the surrounding landscape. With its private infinity pool, landscaped gardens, and smart home technology, this villa offers the ultimate retreat for those seeking a contemporary and lavish lifestyle.",
                                amenities = listOf(
                                    Amenity.FITNESS_CENTER,
                                    Amenity.PARK,
                                    Amenity.SCHOOL,
                                    Amenity.RESTAURANT,
                                ),
                                isAvailableForSale = true,
                                entryDate = LocalDateTime.of(2023, 8, 25, 10, 0),
                                saleDate = null,
                                isSold = false,
                                agentName = "Jane Smith"
                            ),
                        )
                    )


                    val locationsAsJson = gson.toJson(
                        listOf(
                            LocationEntity(
                                id = 1,
                                propertyId = 1,
                                latitude = 40.765076,
                                longitude = -73.976693,
                                address = "Chambers Street",
                                city = "New York City",
                                neighborhood = "Midtown Manhattan",
                                postalCode = "10019",
                            ),
                            LocationEntity(
                                id = 2,
                                propertyId = 2,
                                latitude = 40.710525,
                                longitude = -74.008368,
                                address = "Fulton Street",
                                city = "New York City",
                                postalCode = "10038",
                                neighborhood = "Financial District",
                            ),
                        )
                    )

                    val picturesAsJson = gson.toJson(
                        listOf(
                            PictureEntity(
                                uri = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dmlsbGF8ZW58MHx8MHx8fDA%3D&w=300&q=300",
                                propertyId = 1,
                                description = "Front view",
                                isThumbnail = true,
                            ),
                            PictureEntity(
                                uri = "https://img.freepik.com/photos-gratuite/maison-design-villa-moderne-salon-decloisonne-chambre-privee-aile-grande-terrasse-intimite_1258-169741.jpg?w=300",
                                propertyId = 2,
                                description = "Front view",
                                isThumbnail = true,
                            ),
                        )
                    )

                    workManager.enqueue(
                        OneTimeWorkRequestBuilder<InitializeDatabaseWorker>()
                            .setInputData(
                                workDataOf(
                                    InitializeDatabaseWorker.KEY_INPUT_DATA_PROPERTIES to propertiesAsJson,
                                    InitializeDatabaseWorker.KEY_INPUT_DATA_LOCATIONS to locationsAsJson,
                                    InitializeDatabaseWorker.KEY_INPUT_DATA_PICTURES to picturesAsJson,
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