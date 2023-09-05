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
import com.emplk.realestatemanager.data.amenity.AmenityDao
import com.emplk.realestatemanager.data.amenity.AmenityDtoEntity
import com.emplk.realestatemanager.data.location.LocationDao
import com.emplk.realestatemanager.data.location.LocationDtoEntity
import com.emplk.realestatemanager.data.picture.PictureDao
import com.emplk.realestatemanager.data.picture.PictureDtoEntity
import com.emplk.realestatemanager.data.property.PropertyDao
import com.emplk.realestatemanager.data.property.PropertyDtoEntity
import com.emplk.realestatemanager.data.utils.type_converters.BigDecimalTypeConverter
import com.emplk.realestatemanager.data.utils.type_converters.LocalDateTimeTypeConverter
import com.emplk.realestatemanager.domain.amenity.AmenityType
import com.google.gson.Gson
import java.math.BigDecimal
import java.time.LocalDateTime


@Database(
    entities = [
        PropertyDtoEntity::class,
        PictureDtoEntity::class,
        LocationDtoEntity::class,
        AmenityDtoEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    LocalDateTimeTypeConverter::class,
    BigDecimalTypeConverter::class,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getPropertyDao(): PropertyDao
    abstract fun getLocationDao(): LocationDao
    abstract fun getPictureDao(): PictureDao
    abstract fun getAmenityDao(): AmenityDao

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
                            PropertyDtoEntity(
                                type = "Flat",
                                price = BigDecimal(1000000),
                                surface = 150,
                                rooms = 5,
                                bathrooms = 2,
                                bedrooms = 3,
                                description = "Discover luxury living at its finest with this stunning and spacious home. Boasting elegant design, high-end finishes, and a prime location, this property offers everything you need for a comfortable and lavish lifestyle.",
                                isAvailableForSale = true,
                                entryDate = LocalDateTime.of(2023, 8, 24, 10, 0),
                                saleDate = null,
                                isSold = false,
                                agentName = "John Doe"
                            ),
                            PropertyDtoEntity(
                                type = "Villa",
                                price = BigDecimal(5000000),
                                surface = 200,
                                rooms = 8,
                                bathrooms = 4,
                                bedrooms = 4,
                                description = " Experience the epitome of modern luxury in this exquisite villa that seamlessly blends sophistication with comfort. This architectural masterpiece features sleek lines, floor-to-ceiling windows, and cutting-edge design elements that create an unparalleled living experience. Enjoy spacious living areas, a state-of-the-art kitchen, and breathtaking panoramic views of the surrounding landscape. With its private infinity pool, landscaped gardens, and smart home technology, this villa offers the ultimate retreat for those seeking a contemporary and lavish lifestyle.",
                                isAvailableForSale = false,
                                entryDate = LocalDateTime.of(2023, 8, 25, 10, 0),
                                saleDate = LocalDateTime.of(2023, 9, 3, 11, 0),
                                isSold = true,
                                agentName = "Jane Smith"
                            ),
                            PropertyDtoEntity(
                                type = "Penthouse",
                                price = BigDecimal(2000000),
                                surface = 100,
                                rooms = 3,
                                bathrooms = 2,
                                bedrooms = 2,
                                description = "This stunning penthouse offers the ultimate in luxury living with its sleek design, high-end finishes, and breathtaking views. The open floor plan features a spacious living area, a gourmet kitchen, and a private terrace that overlooks the city skyline. The master suite boasts a spa-like bathroom with a soaking tub and walk-in shower. With its prime location and modern amenities, this penthouse is perfect for those seeking an upscale lifestyle.",
                                isAvailableForSale = true,
                                entryDate = LocalDateTime.of(2023, 8, 26, 10, 0),
                                saleDate = null,
                                isSold = false,
                                agentName = "John Doe"
                            ),
                        )
                    )

                    val locationsAsJson = gson.toJson(
                        listOf(
                            LocationDtoEntity(
                                propertyId = 1,
                                latitude = 40.765076,
                                longitude = -73.976693,
                                address = "Chambers Street",
                                city = "New York City",
                                postalCode = "10019",
                            ),
                            LocationDtoEntity(
                                propertyId = 2,
                                latitude = 40.710525,
                                longitude = -74.008368,
                                address = "Fulton Street",
                                city = "New York City",
                                postalCode = "10038",
                            ),
                            LocationDtoEntity(
                                propertyId = 3,
                                latitude = 40.765076,
                                longitude = -73.976693,
                                address = "Chambers Street",
                                city = "New York City",
                                postalCode = "10019",
                            ),
                        )
                    )

                    val picturesAsJson = gson.toJson(
                        listOf(
                            PictureDtoEntity(
                                uri = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dmlsbGF8ZW58MHx8MHx8fDA%3D&w=300&q=300",
                                propertyId = 1,
                                description = "Front view",
                                isThumbnail = true,
                            ),
                            PictureDtoEntity(
                                uri = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dmlsbGF8ZW58MHx8MHx8fDA%3D&w=300&q=300",
                                propertyId = 1,
                                description = "Living room",
                                isThumbnail = false,
                            ),
                            PictureDtoEntity(
                                uri = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dmlsbGF8ZW58MHx8MHx8fDA%3D&w=300&q=300",
                                propertyId = 1,
                                description = "Kitchen",
                                isThumbnail = false,
                            ),
                            PictureDtoEntity(
                                uri = "https://img.freepik.com/photos-gratuite/maison-design-villa-moderne-salon-decloisonne-chambre-privee-aile-grande-terrasse-intimite_1258-169741.jpg?w=300",
                                propertyId = 2,
                                description = "Front view",
                                isThumbnail = true,
                            ),
                            PictureDtoEntity(
                                uri = "https://img.freepik.com/photos-gratuite/maison-design-villa-moderne-salon-decloisonne-chambre-privee-aile-grande-terrasse-intimite_1258-169741.jpg?w=300",
                                propertyId = 2,
                                description = "Living room",
                                isThumbnail = false,
                            ),
                            PictureDtoEntity(
                                uri = "https://img.freepik.com/photos-gratuite/maison-design-villa-moderne-salon-decloisonne-chambre-privee-aile-grande-terrasse-intimite_1258-169741.jpg?w=300",
                                propertyId = 2,
                                description = "Kitchen",
                                isThumbnail = false,
                            ),
                            PictureDtoEntity(
                                uri = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dmlsbGF8ZW58MHx8MHx8fDA%3D&w=300&q=300",
                                propertyId = 3,
                                description = "Front view",
                                isThumbnail = true,
                            ),
                            PictureDtoEntity(
                                uri = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dmlsbGF8ZW58MHx8MHx8fDA%3D&w=300&q=300",
                                propertyId = 3,
                                description = "Living room",
                                isThumbnail = false,
                            ),
                            PictureDtoEntity(
                                uri = "https://images.unsplash.com/photo-1580587771525-78b9dba3b914?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxzZWFyY2h8Mnx8dmlsbGF8ZW58MHx8MHx8fDA%3D&w=300&q=300",
                                propertyId = 3,
                                description = "Kitchen",
                                isThumbnail = false,
                            ),
                        )
                    )

                    val amenitiesAsJson = gson.toJson(
                        listOf(
                            AmenityDtoEntity(
                                name = AmenityType.PARK.name,
                                propertyId = 1,
                            ),
                            AmenityDtoEntity(
                                name = AmenityType.GYM.name,
                                propertyId = 1,
                            ),
                            AmenityDtoEntity(
                                name = AmenityType.RESTAURANT.name,
                                propertyId = 1,
                            ),
                            AmenityDtoEntity(
                                name = AmenityType.SCHOOL.name,
                                propertyId = 2,
                            ),
                            AmenityDtoEntity(
                                name = AmenityType.SHOPPING_MALL.name,
                                propertyId = 2,
                            ),
                            AmenityDtoEntity(
                                name = AmenityType.PUBLIC_TRANSPORTATION.name,
                                propertyId = 2,
                            ),
                            AmenityDtoEntity(
                                name = AmenityType.PARK.name,
                                propertyId = 3,
                            ),
                            AmenityDtoEntity(
                                name = AmenityType.GYM.name,
                                propertyId = 3,
                            ),
                            AmenityDtoEntity(
                                name = AmenityType.HOSPITAL.name,
                                propertyId = 3,
                            ),
                            AmenityDtoEntity(
                                name = AmenityType.SCHOOL.name,
                                propertyId = 3,
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
                                    InitializeDatabaseWorker.KEY_INPUT_DATA_AMENITIES to amenitiesAsJson,
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