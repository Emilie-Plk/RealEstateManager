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
import com.emplk.realestatemanager.data.property.PropertyDto
import com.emplk.realestatemanager.data.property.amenity.AmenityDao
import com.emplk.realestatemanager.data.property.amenity.AmenityDto
import com.emplk.realestatemanager.data.property.location.LocationDao
import com.emplk.realestatemanager.data.property.location.LocationDto
import com.emplk.realestatemanager.data.property.picture.PictureDao
import com.emplk.realestatemanager.data.property.picture.PictureDto
import com.emplk.realestatemanager.data.property_draft.FormDraftDao
import com.emplk.realestatemanager.data.property_draft.BaseFormDraftDto
import com.emplk.realestatemanager.data.property_draft.amenity.AmenityDraftDao
import com.emplk.realestatemanager.data.property_draft.amenity.AmenityDraftDto
import com.emplk.realestatemanager.data.property_draft.picture_preview.PicturePreviewDao
import com.emplk.realestatemanager.data.property_draft.picture_preview.PicturePreviewDto
import com.emplk.realestatemanager.data.utils.type_converters.BigDecimalTypeConverter
import com.emplk.realestatemanager.data.utils.type_converters.LocalDateTimeTypeConverter
import com.emplk.realestatemanager.domain.property.amenity.AmenityType
import com.google.gson.Gson
import java.math.BigDecimal
import java.time.LocalDateTime

@Database(
    entities = [
        PropertyDto::class,
        PictureDto::class,
        LocationDto::class,
        AmenityDto::class,
        BaseFormDraftDto::class,
        PicturePreviewDto::class,
        AmenityDraftDto::class,
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
    abstract fun getPropertyFormDao(): FormDraftDao
    abstract fun getPicturePreviewDao(): PicturePreviewDao
    abstract fun getAmenityFormDao(): AmenityDraftDao

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

            return builder.build()
        }
    }
}