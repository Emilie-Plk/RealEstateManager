package com.emplk.realestatemanager.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.emplk.realestatemanager.data.property.PropertyDao
import com.emplk.realestatemanager.data.utils.LocationTypeConverter
import com.emplk.realestatemanager.data.utils.PointOfInterestListTypeConverter
import com.emplk.realestatemanager.data.utils.PropertyPictureListTypeConverter
import com.emplk.realestatemanager.domain.add_property.entities.LocationEntity
import com.emplk.realestatemanager.domain.add_property.entities.PointOfInterestEntity
import com.emplk.realestatemanager.domain.add_property.entities.PropertyEntity
import com.google.gson.Gson


@Database(
    entities = [
        PropertyEntity::class,
        PointOfInterestEntity::class,
        LocationEntity::class,
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    LocationTypeConverter::class,
    PropertyPictureListTypeConverter::class,
    PointOfInterestListTypeConverter::class,
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getPropertyDao(): PropertyDao

    companion object {
        private const val DATABASE_NAME = "RealEstateManager_database"

        fun create(
            application: Application,
            gson: Gson,
        ): AppDatabase {
            return Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                DATABASE_NAME
            ).build()
        }
    }
}