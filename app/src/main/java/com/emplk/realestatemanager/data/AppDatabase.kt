package com.emplk.realestatemanager.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.emplk.realestatemanager.data.property.PropertyDao
import com.emplk.realestatemanager.data.property.PropertyDto
import com.emplk.realestatemanager.data.property.location.LocationDao
import com.emplk.realestatemanager.data.property.location.LocationDto
import com.emplk.realestatemanager.data.property.picture.PictureDao
import com.emplk.realestatemanager.data.property.picture.PictureDto
import com.emplk.realestatemanager.data.property_draft.FormDraftDao
import com.emplk.realestatemanager.data.property_draft.FormDraftDto
import com.emplk.realestatemanager.data.property_draft.picture_preview.PicturePreviewDao
import com.emplk.realestatemanager.data.property_draft.picture_preview.PicturePreviewDto
import com.emplk.realestatemanager.data.utils.type_converters.BigDecimalTypeConverter
import com.emplk.realestatemanager.data.utils.type_converters.LocalDateTimeTypeConverter

@Database(
    entities = [
        PropertyDto::class,
        PictureDto::class,
        LocationDto::class,
        FormDraftDto::class,
        PicturePreviewDto::class,
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
    abstract fun getPropertyFormDao(): FormDraftDao
    abstract fun getPicturePreviewDao(): PicturePreviewDao

    companion object {
        private const val DATABASE_NAME = "RealEstateManager_database"

        fun create(
            application: Application,
        ): AppDatabase =
            Room.databaseBuilder(
                application,
                AppDatabase::class.java,
                DATABASE_NAME
            )
                .build()
    }
}